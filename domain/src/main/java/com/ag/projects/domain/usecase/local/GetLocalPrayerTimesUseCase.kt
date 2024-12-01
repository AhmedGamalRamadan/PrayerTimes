package com.ag.projects.domain.usecase.local

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository

class GetLocalPrayerTimesUseCase(
    private val prayerTimesLocalRepository: PrayerTimesLocalRepository
) {

   suspend operator fun invoke(): List<DataEntity> =prayerTimesLocalRepository.getAllData()
}