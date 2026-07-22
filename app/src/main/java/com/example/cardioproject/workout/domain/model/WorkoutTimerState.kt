package com.example.cardioproject.workout.domain.model

data class WorkoutTimerState(
    val currentPhase: WorkoutPhase,
    val timeRemainingSec: Int,
    val currentSet: Int,
    val currentRep: Int,
    val totalElapsedTimeSec: Int
)
