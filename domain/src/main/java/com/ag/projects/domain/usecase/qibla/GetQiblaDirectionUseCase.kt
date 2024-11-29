package com.ag.projects.domain.usecase.qibla

import com.ag.projects.data.model.QiblaResponse
import com.ag.projects.data.respoitory.PrayerTimeRepository

class GetQiblaDirectionUseCase(
    private val prayerTimeRepository: PrayerTimeRepository
) {

    suspend operator fun invoke(): QiblaResponse = prayerTimeRepository.getQiblaDirection()
}