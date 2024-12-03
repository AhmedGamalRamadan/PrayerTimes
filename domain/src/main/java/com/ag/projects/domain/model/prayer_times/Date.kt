package com.ag.projects.domain.model.prayer_times

data class Date(
    val gregorian: Gregorian?,
    val hijri: Hijri?,
    val readable: String,
    val timestamp: String
)