package com.example.cardioproject.settings.presentation.model

import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi
import java.util.UUID

data class DeviceInfoUi(
    val macAddress: String,
    val name: String
)

data class ActiveDeviceUi(
    val name: String,
    val macAddress: String,
    val battery: Int?
)

data class HeartRateZone(
    val label: String,
    var range: ClosedFloatingPointRange<Float>
)

data class TimerProfileUi(
    val id: String,
    val name: String
)

data class SettingsUiState(
    val savedDevices: List<DeviceInfoUi> = emptyList(),
    val activeDevice: ActiveDeviceUi? = null,
    val phaseSoundEnabled: Boolean = true,
    val signalVolume: Float = 0.6f,
    val tags: List<WorkoutTagUi> = listOf(
        WorkoutTagUi(UUID.randomUUID().toString(), "Общая", "#6750A4", false),
        WorkoutTagUi(UUID.randomUUID().toString(), "Силовая", "#6750A4", false),
        WorkoutTagUi(UUID.randomUUID().toString(), "Легкая", "#6750A4", false),
        WorkoutTagUi(UUID.randomUUID().toString(), "Вело", "#6750A4", false)
    ),
    val timerProfiles: List<TimerProfileUi> = listOf(
        TimerProfileUi("1", "Профиль 1"),
        TimerProfileUi("2", "Профиль 2")
    ),
    val birthDate: String = "",
    val gender: String = "",
    val height: String = "",
    val zones: List<HeartRateZone> = listOf(
        HeartRateZone("Зона 1", 60f..79f),
        HeartRateZone("Зона 2", 60f..79f),
        HeartRateZone("Зона 3", 60f..79f),
        HeartRateZone("Зона 4", 60f..79f),
    ),
    val criticalPulseAlertEnabled: Boolean = true,
    val keepScreenOnEnabled: Boolean = true,
)