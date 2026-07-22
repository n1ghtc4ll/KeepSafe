package com.example.cardioproject.workout.data.mapper

import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.workout.data.entity.WorkoutSessionDbEntity
import com.example.cardioproject.workout.domain.model.WorkoutSession

fun WorkoutSession.toEntity(): WorkoutSessionDbEntity {
    return WorkoutSessionDbEntity(
        id = this.id,
        dateTimestamp = this.dateTimestamp,
        durationSec = this.durationSec,
        tagId = this.tag?.id,
        averageHeartRate = this.averageHeartRate,
        maxHeartRate = this.maxHeartRate,
        minHeartRate = this.minHeartRate,
        heartRateHistory = this.heartRateHistory
    )
}

fun WorkoutSessionDbEntity.toDomain(domainTag: Tag?): WorkoutSession {
    return WorkoutSession(
        id = this.id,
        dateTimestamp = this.dateTimestamp,
        durationSec = this.durationSec,
        tag = domainTag,
        averageHeartRate = this.averageHeartRate,
        maxHeartRate = this.maxHeartRate,
        minHeartRate = this.minHeartRate,
        heartRateHistory = this.heartRateHistory
    )
}