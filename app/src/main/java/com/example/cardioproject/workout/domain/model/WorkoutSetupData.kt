package com.example.cardioproject.workout.domain.model

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag

data class WorkoutSetupData(
    val tags: List<Tag>,
    val tabataProfiles: List<TabataProfile>
)
