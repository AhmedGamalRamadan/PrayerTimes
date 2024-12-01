package com.ag.projects.domain.usecase.prayer_times

import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.repository.remote.PrayerTimeRepository

class GetRemotePrayerTimesUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    suspend operator fun invoke(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ): PrayerTimesResponse = prayerTimeRepository.getPrayerTimes(
        year = year,
        month = month,
        latitude = latitude,
        longitude = longitude
    )
}