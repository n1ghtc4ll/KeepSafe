package com.example.cardioproject.workout.presentation.model

import com.example.cardioproject.settings.domain.model.DeviceInfo

data class MainScreenUiState(
    val currentBpm: Int? = null,
    val deviceName: String? = null,
    val deviceBattery: Int? = null,
    val isDeviceConnected: Boolean = false
)
