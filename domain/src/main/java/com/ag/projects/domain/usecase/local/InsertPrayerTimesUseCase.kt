package com.ag.projects.domain.usecase.local

import android.util.Log
import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository

class InsertPrayerTimesUseCase(
    private val prayerTimesLocalRepository: PrayerTimesLocalRepository
) {

    suspend operator fun invoke(data: List<DataEntity>){
        prayerTimesLocalRepository.insertData(data)
        Log.d("InsertPrayerTimesUseCase","the data inserted ${data}")
    }
}