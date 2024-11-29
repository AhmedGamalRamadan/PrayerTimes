package com.ag.projects.domain.usecase.qibla

import com.ag.projects.domain.model.QiblaResponse
import com.ag.projects.domain.repository.PrayerTimeRepository

class GetQiblaDirectionUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    suspend operator fun invoke(): QiblaResponse = prayerTimeRepository.getQiblaDirection()
}