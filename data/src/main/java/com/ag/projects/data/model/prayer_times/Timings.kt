package com.ag.projects.data.model.prayer_times

import com.google.gson.annotations.SerializedName

data class Timings(
    @SerializedName("Asr")
    val asr: String,
    @SerializedName("Dhuhr")
    val dhuhr: String,
    @SerializedName("Fajr")
    val fajr: String,
    @SerializedName("Firstthird")
    val firstThird: String,
    @SerializedName("Imsak")
    val imsak: String,
    @SerializedName("Isha")
    val isha: String,
    @SerializedName("Lastthird")
    val lastThird: String,
    @SerializedName("Maghrib")
    val maghrib: String,
    @SerializedName("Midnight")
    val midNight: String,
    @SerializedName("Sunrise")
    val sunrise: String,
    @SerializedName("Sunset")
    val sunset: String
)