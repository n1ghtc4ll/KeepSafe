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
        warmUpColorHex = this.warmUpColorHex,
        workoutTimeSec = this.workoutTimeSec,
        workoutColorHex = this.workoutColorHex,
        relaxTimeSec = this.relaxTimeSec,
        relaxColorHex = this.relaxColorHex,
        breakTimeSec = this.breakTimeSec,
        breakColorHex = this.breakColorHex,
        coolDownTimeSec = this.coolDownTimeSec,
        coolDownColorHex = this.coolDownColorHex
    )
}

fun TabataProfileEntity.toDomain(): TabataProfile {
    return TabataProfile(
        id = this.id,
        name = this.name,
        setsCount = this.setsCount,
        repsCount = this.repsCount,
        warmUpTimeSec = this.warmUpTimeSec,
        warmUpColorHex = this.warmUpColorHex,
        workoutTimeSec = this.workoutTimeSec,
        workoutColorHex = this.workoutColorHex,
        relaxTimeSec = this.relaxTimeSec,
        relaxColorHex = this.relaxColorHex,
        breakTimeSec = this.breakTimeSec,
        breakColorHex = this.breakColorHex,
        coolDownTimeSec = this.coolDownTimeSec,
        coolDownColorHex = this.coolDownColorHex
    )
}