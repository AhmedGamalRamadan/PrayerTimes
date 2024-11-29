package com.ag.projects.aatask.util.helper

import android.os.Build
import java.time.LocalTime
import java.time.format.DateTimeFormatter


fun convertTo12Hour(timeString: String): String {
    // Extract the time part before the space (ignoring the timezone offset)
    val timePart = timeString.substringBefore(" ")

    // Parse the time using LocalTime
    val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.parse(timePart, DateTimeFormatter.ofPattern("HH:mm"))
    } else {
        return timeString
    }

    // Format the time to 12-hour format with AM/PM
    val formatter = DateTimeFormatter.ofPattern("h:mm a")
    return time.format(formatter)
}