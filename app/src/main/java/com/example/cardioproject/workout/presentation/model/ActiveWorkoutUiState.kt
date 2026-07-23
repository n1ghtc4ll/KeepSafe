package com.example.cardioproject.workout.presentation.model

import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.workout.domain.model.WorkoutPhase

data class ActiveWorkoutUiState(
    val currentPhase: WorkoutPhase = WorkoutPhase.WARMUP,
    val timeRemainingSec: Int = 0,
    val currentSet: Int = 1,
    val currentRep: Int = 1,
    val totalElapsedTimeSec: Int = 0,
    val currentHeartRate: Int = 0,
    val heartRateHistory: List<Int> = emptyList(),
    val hrZones: List<HeartRateZone> = emptyList(),
    val isPaused: Boolean = false
)
