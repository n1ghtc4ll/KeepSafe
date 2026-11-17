package com.example.cardioproject.workout.presentation.util

import androidx.compose.ui.graphics.Color
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.TabataSetup

fun TabataSetup?.getColorForPhase(phase: WorkoutPhase): Color {
    if (this == null) return Color(0xFFE0E0E0)

    val hexValue = when (this) {
        is TabataSetup.Custom -> when (phase) {
            WorkoutPhase.WARMUP -> this.warmUpColorHex
            WorkoutPhase.WORKOUT -> this.workoutColorHex
            WorkoutPhase.RELAX -> this.relaxColorHex
            WorkoutPhase.BREAK -> this.breakColorHex
            WorkoutPhase.COOLDOWN -> this.coolDownColorHex
            WorkoutPhase.FINISHED -> 0xFFFFFFFF
        }
        is TabataSetup.Preset -> when (phase) {
            WorkoutPhase.WARMUP -> this.profile.warmUpColorHex
            WorkoutPhase.WORKOUT -> this.profile.workoutColorHex
            WorkoutPhase.RELAX -> this.profile.relaxColorHex
            WorkoutPhase.BREAK -> this.profile.breakColorHex
            WorkoutPhase.COOLDOWN -> this.profile.coolDownColorHex
            WorkoutPhase.FINISHED -> 0xFFFFFFFF
        }
    }

    return Color(hexValue)
}

val TabataSetup.totalSets: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.setsCount
        is TabataSetup.Preset -> this.profile.setsCount
    }
val TabataSetup.totalReps: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.repsCount
        is TabataSetup.Preset -> this.profile.repsCount
    }

val TabataSetup.workoutTimeSec: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.workoutTimeSec
        is TabataSetup.Preset -> this.profile.workoutTimeSec
    }
val TabataSetup.workoutColorHex: Long
    get() = when (this) {
        is TabataSetup.Custom -> this.workoutColorHex
        is TabataSetup.Preset -> this.profile.workoutColorHex
    }

val TabataSetup.warmUpTimeSec: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.warmUpTimeSec
        is TabataSetup.Preset -> this.profile.warmUpTimeSec
    }
val TabataSetup.warmUpColorHex: Long
    get() = when (this) {
        is TabataSetup.Custom -> this.warmUpColorHex
        is TabataSetup.Preset -> this.profile.warmUpColorHex
    }

val TabataSetup.relaxTimeSec: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.relaxTimeSec
        is TabataSetup.Preset -> this.profile.relaxTimeSec
    }
val TabataSetup.relaxColorHex: Long
    get() = when (this) {
        is TabataSetup.Custom -> this.relaxColorHex
        is TabataSetup.Preset -> this.profile.relaxColorHex
    }

val TabataSetup.breakTimeSec: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.breakTimeSec
        is TabataSetup.Preset -> this.profile.breakTimeSec
    }
val TabataSetup.breakColorHex: Long
    get() = when (this) {
        is TabataSetup.Custom -> this.breakColorHex
        is TabataSetup.Preset -> this.profile.breakColorHex
    }

val TabataSetup.coolDownTimeSec: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.coolDownTimeSec
        is TabataSetup.Preset -> this.profile.coolDownTimeSec
    }
val TabataSetup.coolDownColorHex: Long
    get() = when (this) {
        is TabataSetup.Custom -> this.coolDownColorHex
        is TabataSetup.Preset -> this.profile.coolDownColorHex
    }