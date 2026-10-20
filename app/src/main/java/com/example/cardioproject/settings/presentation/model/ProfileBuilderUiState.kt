package com.example.cardioproject.settings.presentation.model

data class ProfileBuilderUiState(
    val id: String? = null,
    val name: String = "",
    val setsCount: Int = 1,
    val repsCount: Int = 8,

    val warmUpTimeSec: Int = 30,
    val warmUpColorHex: Long = 0xFFFF9800, // Orange

    val workoutTimeSec: Int = 45,
    val workoutColorHex: Long = 0xFF4CAF50, // Green

    val relaxTimeSec: Int = 15,
    val relaxColorHex: Long = 0xFF2196F3, // Blue

    val breakTimeSec: Int = 60,
    val breakColorHex: Long = 0xFF9C27B0, // Purple

    val coolDownTimeSec: Int = 120,
    val coolDownColorHex: Long = 0xFF00BCD4  // Cyan
)
