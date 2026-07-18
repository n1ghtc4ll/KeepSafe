package com.example.cardioproject.workout.domain.model

import com.example.cardioproject.core.common.domain.model.TabataProfile

sealed interface TabataSetup {
    data class Preset(
        val profile: TabataProfile
    ) : TabataSetup

    data class Custom(
        val setsCount: Int,
        val repsCount: Int,
        val warmUpTimeSec: Int,
        val workoutTimeSec: Int,
        val relaxTimeSec: Int,
        val breakTimeSec: Int,
        val coolDownTimeSec: Int
    ) : TabataSetup
}