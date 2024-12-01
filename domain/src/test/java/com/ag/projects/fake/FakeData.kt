package com.ag.projects.fake

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.QiblaResponse
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


val data = Data(
    meta = null,
    timings = timing,
    date = null
)


val successPrayerTimesResponse = PrayerTimesResponse(
    code = 1,
    data = listOf(data),
    status = "success"
)

val emptyPrayerTimesResponse = PrayerTimesResponse(
    code = 0,
    data = emptyList(),
    status = "fail"
)


val successQiblaResponse = QiblaResponse(
    code = 1,
    data = com.ag.projects.domain.model.Data(
        direction = lat,
        latitude = lat,
        longitude = lng
    ),
    status = "success"
)

val failQiblaResponse = QiblaResponse(
    code = 0,
    data = com.ag.projects.domain.model.Data(
        direction = lat,
        latitude = lat,
        longitude = lng
    ),
    status = "fail"
)

