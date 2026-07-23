package com.example.cardioproject.core.common.domain.model

data class TabataProfile(
    val id: String,
    val name: String,
    val setsCount: Int,
    val repsCount: Int,
    val warmUpTimeSec: Int,
    val warmUpColorHex: Long = 0xFFFFB74D,
    val workoutTimeSec: Int,
    val workoutColorHex: Long = 0xFF81C784,
    val relaxTimeSec: Int,
    val relaxColorHex: Long = 0xFF64B5F6,
    val breakTimeSec: Int,
    val breakColorHex: Long = 0xFF9575CD,
    val coolDownTimeSec: Int,
    val coolDownColorHex: Long = 0xFF4DB6AC
)
