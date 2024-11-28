package com.ag.projects.data.model

data class PrayerTimesResponse(
    val code: Int,
    val data: List<Data>,
    val status: String
)