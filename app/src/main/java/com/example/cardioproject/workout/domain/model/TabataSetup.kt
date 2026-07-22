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
        val workoutTimeSec: Int = 0,
        val relaxTimeSec: Int = 0,
        val breakTimeSec: Int = 0,
        val coolDownTimeSec: Int = 0
    ) : TabataSetup
}