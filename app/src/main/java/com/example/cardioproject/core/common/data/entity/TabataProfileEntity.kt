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
    val warmUpColorHex: Long,

    val workoutTimeSec: Int,
    val workoutColorHex: Long,

    val relaxTimeSec: Int,
    val relaxColorHex: Long,

    val breakTimeSec: Int,
    val breakColorHex: Long,

    val coolDownTimeSec: Int,
    val coolDownColorHex: Long
)
