package com.ag.projects.fake

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.prayer_times.Timings


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

val dataEntityItem =DataEntity(
    timings = timing
)

