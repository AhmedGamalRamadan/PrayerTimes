package com.ag.projects.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ag.projects.domain.entity.DataEntity

@Database(
    entities = [DataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PrayerTimesDataBase : RoomDatabase() {

    abstract fun dao(): PrayerTimeDao
}