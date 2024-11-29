package com.ag.projects.domain.usecase.prayer_times

import com.ag.projects.data.model.prayer_times.PrayerTimesResponse
import com.ag.projects.data.respoitory.PrayerTimeRepository

class GetPrayerTimesUseCase(
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