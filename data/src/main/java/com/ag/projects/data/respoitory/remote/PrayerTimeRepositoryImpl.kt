package com.ag.projects.data.respoitory.remote

import com.ag.projects.data.remote.PrayerTimesService
import com.ag.projects.domain.model.QiblaResponse
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.repository.remote.PrayerTimeRepository

class PrayerTimeRepositoryImpl(
    private val prayerTimesService: PrayerTimesService
) : PrayerTimeRepository {

    override suspend fun getPrayerTimes(
        year: Int,
        month: Int,
        latitude: Double,
        longitude: Double
    ): PrayerTimesResponse = prayerTimesService.getPrayerTimes(
        year = year,
        month = month,
        latitude = latitude,
        longitude = longitude
    )

    override suspend fun getQiblaDirection(): QiblaResponse = prayerTimesService.getQiblaDirection()

}