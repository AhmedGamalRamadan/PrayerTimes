package com.ag.projects.domain.usecase.local

import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository

class ClearPrayerTimesUseCase(
    private val prayerTimesLocalRepository: PrayerTimesLocalRepository
) {

    suspend operator fun invoke() {
        prayerTimesLocalRepository.clearDatabase()
    }
}