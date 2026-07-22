package com.example.cardioproject.core.common.domain.model

data class TabataProfile(
    val id: String,
    val name: String,
    val setsCount: Int,
    val repsCount: Int,
    val warmUpTimeSec: Int,
    val workoutTimeSec: Int,
    val relaxTimeSec: Int,
    val breakTimeSec: Int,
    val coolDownTimeSec: Int
)
