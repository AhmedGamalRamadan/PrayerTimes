package com.ag.projects.domain.model.prayer_times

data class PrayerTimesResponse(
    val code: Int,
    val data: List<Data>,
    val status: String
)