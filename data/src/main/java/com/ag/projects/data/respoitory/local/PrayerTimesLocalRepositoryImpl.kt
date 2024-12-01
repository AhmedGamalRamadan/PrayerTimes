package com.ag.projects.data.respoitory.local

import com.ag.projects.data.local.PrayerTimesDataBase
import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository

class PrayerTimesLocalRepositoryImpl(
    private val dataBase: PrayerTimesDataBase
) : PrayerTimesLocalRepository {

    override suspend fun insertData(data: List<DataEntity>) = dataBase.dao().insertData(data)

    override suspend fun getAllData(): List<DataEntity> = dataBase.dao().getAllData()
    override suspend fun clearDatabase() {
        dataBase.dao().clearDatabase()
    }
}