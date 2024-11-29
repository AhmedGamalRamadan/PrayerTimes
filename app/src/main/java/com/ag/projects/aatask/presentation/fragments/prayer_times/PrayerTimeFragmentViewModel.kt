package com.ag.projects.aatask.presentation.fragments.prayer_times

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ag.projects.aatask.util.Result
import com.ag.projects.aatask.util.helper.handleRequest
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.usecase.prayer_times.GetPrayerTimesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrayerTimeFragmentViewModel @Inject constructor(
    private val getPrayerTimesUseCase: GetPrayerTimesUseCase
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
        }
    }
}