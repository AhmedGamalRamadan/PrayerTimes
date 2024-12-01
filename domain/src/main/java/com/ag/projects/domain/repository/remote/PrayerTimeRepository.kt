package com.ag.projects.domain.repository.remote

import com.ag.projects.domain.model.QiblaResponse
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse

interface PrayerTimeRepository {

    suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ): PrayerTimesResponse

    suspend fun getQiblaDirection(): QiblaResponse
}