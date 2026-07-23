package com.example.cardioproject.workout.domain.model

import com.example.cardioproject.core.common.domain.model.TabataProfile

sealed interface TabataSetup {
    data class Preset(
        val profile: TabataProfile
    ) : TabataSetup

    data class Custom(
        val setsCount: Int = 0,
        val repsCount: Int = 0,
        val warmUpTimeSec: Int = 0,
        val warmUpColorHex: Long = 0xFFFFB74D,
        val workoutTimeSec: Int = 0,
        val workoutColorHex: Long = 0xFF81C784,
        val relaxTimeSec: Int = 0,
        val relaxColorHex: Long = 0xFF64B5F6,
        val breakTimeSec: Int = 0,
        val breakColorHex: Long = 0xFF9575CD,
        val coolDownTimeSec: Int = 0,
        val coolDownColorHex: Long = 0xFF4DB6AC
    ) : TabataSetup


}