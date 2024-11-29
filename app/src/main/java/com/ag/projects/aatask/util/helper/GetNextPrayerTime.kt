package com.ag.projects.aatask.util.helper


import android.os.Build
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getNextPrayerTime(prayerTimings: Map<String, String>, timezone: String): Pair<String, Duration>? {
    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("HH:mm (XXX)")
    } else {
        TODO("VERSION.SDK_INT < O")
    } // Adjust the pattern for the given time format.
    val currentTime = ZonedDateTime.now(ZoneId.of(timezone))

    val timings = prayerTimings.mapValues {
        LocalTime.parse(it.value.split(" ")[0], DateTimeFormatter.ofPattern("HH:mm"))
            .atDate(LocalDate.now(currentTime.zone))
            .atZone(currentTime.zone)
    }

    val nextPrayer = timings
        .filter { it.value.isAfter(currentTime) }
        .minByOrNull { it.value }

    return if (nextPrayer != null) {
        val timeLeft = Duration.between(currentTime, nextPrayer.value)
        nextPrayer.key to timeLeft
    } else {
        val firstPrayerTomorrow = timings.entries.first()
        val nextPrayerTime = firstPrayerTomorrow.value.plusDays(1)
        val timeLeft = Duration.between(currentTime, nextPrayerTime)
        firstPrayerTomorrow.key to timeLeft
    }
}
