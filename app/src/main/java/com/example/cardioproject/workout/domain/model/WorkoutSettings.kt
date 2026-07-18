package com.example.cardioproject.workout.domain.model

import com.example.cardioproject.core.common.domain.model.Tag

data class WorkoutSettings(
    val title: String,
    val selectedTag: Tag?,
    val isTabataEnabled: Boolean,
    val tabataSetup: TabataSetup?
)
