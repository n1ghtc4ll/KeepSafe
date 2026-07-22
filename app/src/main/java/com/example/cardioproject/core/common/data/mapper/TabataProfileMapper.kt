package com.example.cardioproject.core.common.data.mapper

import com.example.cardioproject.core.common.data.entity.TabataProfileEntity
import com.example.cardioproject.core.common.domain.model.TabataProfile

fun TabataProfile.toEntity(): TabataProfileEntity {
    return TabataProfileEntity(
        id = this.id,
        name = this.name,
        setsCount = this.setsCount,
        repsCount = this.repsCount,
        warmUpTimeSec = this.warmUpTimeSec,
        workoutTimeSec = this.workoutTimeSec,
        relaxTimeSec = this.relaxTimeSec,
        breakTimeSec = this.breakTimeSec,
        coolDownTimeSec = this.coolDownTimeSec
    )
}

fun TabataProfileEntity.toDomain(): TabataProfile {
    return TabataProfile(
        id = this.id,
        name = this.name,
        setsCount = this.setsCount,
        repsCount = this.repsCount,
        warmUpTimeSec = this.warmUpTimeSec,
        workoutTimeSec = this.workoutTimeSec,
        relaxTimeSec = this.relaxTimeSec,
        breakTimeSec = this.breakTimeSec,
        coolDownTimeSec = this.coolDownTimeSec
    )
}