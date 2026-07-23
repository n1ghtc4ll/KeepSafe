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

val TabataSetup?.totalSets: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.setsCount
        is TabataSetup.Preset -> this.profile.setsCount
        null -> 1
    }

val TabataSetup?.totalReps: Int
    get() = when (this) {
        is TabataSetup.Custom -> this.repsCount
        is TabataSetup.Preset -> this.profile.repsCount
        null -> 1
    }

val TabataSetup.workoutColor: Color
    get() = when (this) {
        is TabataSetup.Custom -> Color(this.workoutColorHex)
        is TabataSetup.Preset -> Color(this.profile.workoutColorHex)
    }

val TabataSetup.warmUpColor: Color
    get() = when (this) {
        is TabataSetup.Custom -> Color(this.warmUpColorHex)
        is TabataSetup.Preset -> Color(this.profile.warmUpColorHex)
    }

val TabataSetup.relaxColor: Color
    get() = when (this) {
        is TabataSetup.Custom -> Color(this.relaxColorHex)
        is TabataSetup.Preset -> Color(this.profile.relaxColorHex)
    }

val TabataSetup.breakColor: Color
    get() = when (this) {
        is TabataSetup.Custom -> Color(this.breakColorHex)
        is TabataSetup.Preset -> Color(this.profile.breakColorHex)
    }

val TabataSetup.coolDownColor: Color
    get() = when (this) {
        is TabataSetup.Custom -> Color(this.coolDownColorHex)
        is TabataSetup.Preset -> Color(this.profile.coolDownColorHex)
    }