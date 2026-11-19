package com.example.cardioproject.workout.presentation.model

import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi
import com.example.cardioproject.workout.domain.model.TabataSetup

data class WorkoutSettingsUiState(
    val title: String = "",
    val availableTags: List<WorkoutTagUi> = emptyList(),
    val selectedTag: WorkoutTagUi? = null,
    val isTabataEnabled: Boolean = true,
    val availableProfiles: List<TabataProfile> = emptyList(),
    val tabataSetup: TabataSetup? = null,
)
