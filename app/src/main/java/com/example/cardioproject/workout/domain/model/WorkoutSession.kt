package com.example.cardioproject.workout.domain.model

import com.example.cardioproject.core.common.domain.model.Tag

data class WorkoutSession(
    val id: String,
    val dateTimestamp: Long,
    val durationSec: Int,
    val tag: Tag?,
    val averageHeartRate: Int?,
    val maxHeartRate: Int?,
    val minHeartRate: Int?,
    val heartRateHistory: List<Int>
)
