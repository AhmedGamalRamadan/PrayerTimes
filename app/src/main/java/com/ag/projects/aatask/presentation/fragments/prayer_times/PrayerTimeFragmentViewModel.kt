package com.ag.projects.aatask.presentation.fragments.prayer_times

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.handleRequest
import com.ag.projects.data.local.entity.toData
import com.ag.projects.data.local.entity.toDataEntity
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.usecase.local.ClearPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.GetLocalPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.InsertPrayerTimesUseCase
import com.ag.projects.domain.usecase.prayer_times.GetPrayerTimesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerTimeFragmentViewModel @Inject constructor(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase,
    private val insertPrayerTimesUseCase: InsertPrayerTimesUseCase,
    private val getLocalPrayerTimesUseCase: GetLocalPrayerTimesUseCase,
    private val clearPrayerTimesUseCase: ClearPrayerTimesUseCase
) : ViewModel() {

    private val _prayerTimes = MutableStateFlow<Result<PrayerTimesResponse>>(Result.Loading)
    val prayerTimes = _prayerTimes.asStateFlow()


    fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {

//            clearPrayerTimesDatabase()
            val localData = getLocalPrayerTimesUseCase()
            if (localData.isNotEmpty()) {
                Log.d("InsertPrayerTimesUseCase","the data base not empty contain $localData")

                val prayerTimesResponse = PrayerTimesResponse(
                    code = 0,
                    data = localData.map { it.toData() },
                    status = "local data"
                )
                _prayerTimes.emit(Result.Success(prayerTimesResponse))
            } else {
                Log.d("InsertPrayerTimesUseCase","the data base is empty")

                val prayerTimesResponse = getPrayerTimesUseCase(
                    year = year,
                    month = month,
                    latitude = latitude,
                    longitude = longitude
                )

                handleRequest(
                    request = {
                        prayerTimesResponse
                    },
                    state = _prayerTimes
                )

                if (_prayerTimes.value is Result.Success) {
                    val response = (_prayerTimes.value as Result.Success).data
                    val dataEntities = response.data.map { it.toDataEntity() }
                    launch {
                        insertPrayerTimesUseCase(dataEntities)
                    }
                }
            }

        }
    }

    fun clearPrayerTimesDatabase() {
        viewModelScope.launch {
            clearPrayerTimesUseCase()
        }
    }

}