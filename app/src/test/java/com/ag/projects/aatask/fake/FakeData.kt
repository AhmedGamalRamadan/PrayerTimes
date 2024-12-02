package com.ag.projects.aatask.fake

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.prayer_times.Data
import com.ag.projects.domain.model.prayer_times.PrayerTimesResponse
import com.ag.projects.domain.model.prayer_times.Timings

const val year = 2024
const val month = 12
const val lat = 33.11
const val lng = 33.11

val timing = Timings(
    asr = "3 AM",
    dhuhr = "12 AM",
    fajr = "5 AM",
    firstThird = "10 AM",
    imsak = "3 AM",
    isha = "3 AM",
    lastThird = "3 AM",
    maghrib = "5 AM",
    midNight = "12 AM",
    sunrise = "7 AM",
    sunset = "3 AM",
)
val dataEntityList = listOf(
    DataEntity(timings = timing),
    DataEntity(timings = timing),
    DataEntity(timings = timing),
)

val dataEntityItem = DataEntity(
    timings = timing
)

val prayerTimesSuccessResponse = PrayerTimesResponse(
    code = 200,
    data = listOf(
        Data(
            date = null,
            meta = null,
            timings = timing
        )
    ),
    status = "OK"
)
