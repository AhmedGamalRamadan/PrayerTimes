package com.ag.projects.domain.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ag.projects.domain.model.prayer_times.Timings


@Entity(tableName = "prayer_times")
data class DataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Embedded("timings_") val timings: Timings,
    val readable: String
)