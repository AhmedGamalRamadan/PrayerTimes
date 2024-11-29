package com.ag.projects.data.respoitory

import com.ag.projects.data.model.QiblaResponse
import com.ag.projects.data.model.prayer_times.PrayerTimesResponse

interface PrayerTimeRepository {

    suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ): PrayerTimesResponse

    suspend fun getQiblaDirection(): QiblaResponse
}