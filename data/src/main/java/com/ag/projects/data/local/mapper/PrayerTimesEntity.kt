package com.ag.projects.data.local.mapper

import com.ag.projects.domain.entity.DataEntity
import com.ag.projects.domain.model.prayer_times.Data

fun Data.toDataEntity(): DataEntity {

    return DataEntity(
        timings = timings
    )
}

fun DataEntity.toData(): Data {
    return Data(
        date = null,
        meta = null,
        timings = this.timings
    )
}