package com.ag.projects.domain.model.prayer_times

data class Method(
    val id: Int?,
    val location: Location,
    val name: String?,
    val params: Params?
)