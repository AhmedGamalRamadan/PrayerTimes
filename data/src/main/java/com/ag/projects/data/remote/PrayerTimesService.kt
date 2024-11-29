package com.ag.projects.data.remote

import com.ag.projects.domain.model.QiblaResponse
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.data.util.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerTimesService {

    @GET("calendar/{year}/{month}")
    suspend fun getPrayerTimes(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PrayerTimesResponse

    @GET("qibla/{latitude}/{longitude}")
    suspend fun getQiblaDirection(
        @Path("latitude") latitude: Double = Constants.QIBLA_LAT,
        @Path("longitude") longitude: Double = Constants.QIBLA_LNG
    ): QiblaResponse
}