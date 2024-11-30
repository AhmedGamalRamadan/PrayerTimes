package com.ag.projects.domain.model.prayer_times

import com.google.gson.annotations.SerializedName

data class Params(
    @SerializedName("Fajr")
    val fajr: Double?,
    @SerializedName("Isha")
    val isha: Double?
)