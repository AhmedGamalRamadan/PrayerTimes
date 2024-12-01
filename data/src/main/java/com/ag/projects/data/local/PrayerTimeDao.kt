package com.ag.projects.data.local
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.ag.projects.domain.entity.DataEntity

@Dao
interface PrayerTimeDao {
    @Upsert
    suspend fun insertData(data: List<DataEntity>)

    @Query("SELECT * FROM prayer_times")
    suspend fun getAllData(): List<DataEntity>

    @Query("DELETE FROM prayer_times")
    suspend fun clearDatabase()
}