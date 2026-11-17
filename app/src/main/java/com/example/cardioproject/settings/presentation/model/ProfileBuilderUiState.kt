package com.example.cardioproject.settings.presentation.model

import com.example.cardioproject.workout.domain.model.TabataSetup

data class ProfileBuilderUiState(
    val id: String? = null,
    val name: String = "",
    val tabataSetup: TabataSetup.Custom = TabataSetup.Custom(
        setsCount = 1,
        repsCount = 8,

        warmUpTimeSec = 30,
        warmUpColorHex = 0xFFFF9800,

        workoutTimeSec = 45,
        workoutColorHex = 0xFF4CAF50,

        relaxTimeSec = 15,
        relaxColorHex = 0xFF2196F3,

        breakTimeSec = 60,
        breakColorHex = 0xFF9C27B0,

        coolDownTimeSec = 120,
        coolDownColorHex = 0xFF00BCD4
    )
)
