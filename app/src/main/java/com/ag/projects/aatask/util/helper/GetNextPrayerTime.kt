package com.ag.projects.aatask.util.helper

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@SuppressLint("NewApi")
fun getNextPrayerTime(prayerTimings: Map<String, String>, timezone: String): Pair<String, Duration>? {
    val currentTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ZonedDateTime.now(ZoneId.of(timezone))
    } else {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(timezone))
        calendar.timeZone = TimeZone.getTimeZone(timezone)
        val offsetInMillis = calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)
        val timeInMillis = calendar.timeInMillis + offsetInMillis
        Date(timeInMillis).toInstant().atZone(ZoneId.of(timezone))
    }

    val timings = prayerTimings.mapValues { entry ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val localTime = LocalTime.parse(entry.value.split(" ")[0], DateTimeFormatter.ofPattern("HH:mm"))
            localTime.atDate(LocalDate.now(currentTime.zone)).atZone(currentTime.zone)
        } else {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone(timezone)
            val date = sdf.parse(entry.value.split(" ")[0])!!
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.YEAR, currentTime.year)
            calendar.set(Calendar.MONTH, currentTime.monthValue - 1)
            calendar.set(Calendar.DAY_OF_MONTH, currentTime.dayOfMonth)
            calendar.time.toInstant().atZone(ZoneId.of(timezone))
        }
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