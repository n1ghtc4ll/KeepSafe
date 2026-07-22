package com.example.cardioproject.workout.presentation.model

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.workout.domain.model.TabataSetup

data class WorkoutSettingsUiState(
    val title: String = "",
    val availableTags: List<Tag> = emptyList(),
    val selectedTag: Tag? = null,
    val isTabataEnabled: Boolean = true,
    val availableProfiles: List<TabataProfile> = emptyList(),
    val tabataSetup: TabataSetup? = null,
)
