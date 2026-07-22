package com.example.cardioproject.core.common.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tabata_profiles")
data class TabataProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val setsCount: Int,
    val repsCount: Int,
    val warmUpTimeSec: Int,
    val workoutTimeSec: Int,
    val relaxTimeSec: Int,
    val breakTimeSec: Int,
    val coolDownTimeSec: Int
)
