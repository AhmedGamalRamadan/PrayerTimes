package com.ag.projects.data.local.mapper

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.prayer_times.Data
import com.ag.projects.domain.model.prayer_times.Date

fun Data.toDataEntity(): DataEntity {

    return DataEntity(
        timings = timings,
        readable = date!!.readable
    )
}

fun DataEntity.toData(): Data {
    return Data(
        date = Date(gregorian = null, hijri = null, readable = readable, timestamp = ""),
        meta = null,
        timings = this.timings
    )
}