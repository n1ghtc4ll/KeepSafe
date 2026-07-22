package com.example.cardioproject.workout.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "workout_session_history")
@TypeConverters(WorkoutConverters::class)
data class WorkoutSessionDbEntity(
    @PrimaryKey val id: String,
    val dateTimestamp: Long,
    val durationSec: Int,
    val tagId: String?,
    val averageHeartRate: Int?,
    val maxHeartRate: Int?,
    val minHeartRate: Int?,
    val heartRateHistory: List<Int>
)