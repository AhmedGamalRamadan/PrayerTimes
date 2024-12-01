package com.ag.projects.domain.repository.local

import com.ag.projects.domain.entity.DataEntity

interface PrayerTimesLocalRepository {

    suspend fun insertData(data: List<DataEntity>)

    suspend fun getAllData(): List<DataEntity>

    suspend fun clearDatabase()
}