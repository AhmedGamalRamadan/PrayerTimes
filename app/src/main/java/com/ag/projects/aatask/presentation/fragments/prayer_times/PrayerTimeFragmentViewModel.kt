package com.ag.projects.aatask.presentation.fragments.prayer_times

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.handleRequest
import com.ag.projects.aatask.util.network.NetworkConnection
import com.ag.projects.data.local.mapper.toData
import com.ag.projects.data.local.mapper.toDataEntity
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.usecase.local.ClearPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.GetLocalPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.InsertPrayerTimesUseCase
import com.ag.projects.domain.usecase.prayer_times.GetRemotePrayerTimesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerTimeFragmentViewModel @Inject constructor(
    private val getRemotePrayerTimesUseCase: GetRemotePrayerTimesUseCase,
    private val insertPrayerTimesUseCase: InsertPrayerTimesUseCase,
    private val getLocalPrayerTimesUseCase: GetLocalPrayerTimesUseCase,
    private val clearPrayerTimesUseCase: ClearPrayerTimesUseCase,
    private val networkConnection: NetworkConnection
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

            try {
                if (networkConnection.isWifiConnected()) {
                    //clear previous data
                    clearPrayerTimesDatabase()
                    Log.d("InsertPrayerTimesUseCase", "the data base is cleared")

                    //load new data
                    handleRequest(
                        request = {
                            getRemotePrayerTimesUseCase(
                                year = year,
                                month = month,
                                latitude = latitude,
                                longitude = longitude
                            )
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
                } else {
                    //load from database
                    loadLocalData()
                }
            } catch (e: Exception) {
                _prayerTimes.emit(Result.Error(e.message ?: "An unexpected error occurred"))
            }

        }
    }

    private fun loadLocalData() {
        viewModelScope.launch {
            val localData = getLocalPrayerTimesUseCase()
            if (localData.isNotEmpty()) {
                Log.d("InsertPrayerTimesUseCase", "the data base not empty contain $localData")
                val prayerTimesResponse = PrayerTimesResponse(
                    code = 0,
                    data = localData.map { it.toData() },
                    status = "local data"
                )
                _prayerTimes.emit(Result.Success(prayerTimesResponse))
            } else {
                _prayerTimes.emit(Result.Error("No local data available"))
            }
        }
    }

    private fun clearPrayerTimesDatabase() {
        viewModelScope.launch {
            clearPrayerTimesUseCase()
        }
    }

    suspend fun localDataExist(): Boolean {
        val localData = getLocalPrayerTimesUseCase()
        return localData.isNotEmpty()
    }

}