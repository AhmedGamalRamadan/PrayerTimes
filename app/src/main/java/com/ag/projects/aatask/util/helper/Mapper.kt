package com.ag.projects.aatask.util.helper

import com.ag.projects.domain.model.prayer_times.Timings


fun Timings.toMap(): Map<String, String> {
    return mapOf(
        "Fajr" to fajr,
        "Sunrise" to sunrise,
        "Dhuhr" to dhuhr,
        "Asr" to asr,
        "Maghrib" to maghrib,
        "Isha" to isha,
        "Imsak" to imsak,
        "Midnight" to midNight,
        "Firstthird" to firstThird,
        "Lastthird" to lastThird,
        "Sunset" to sunset
    )
}
