package com.example.cardioproject.settings.presentation.model

import java.util.UUID

data class HeartRateZone(
    val label: String,
    var range: ClosedFloatingPointRange<Float>
)

data class TimerProfileUi(
    val id: String,
    val name: String
)

data class WorkoutTag(
    val id: String,
    val name: String,
    val color: String, // Добавили цвет, так как он есть в TagEntity
    val editable: Boolean = true
)

data class SettingsUiState(
    val deviceName: String = "Coospo HW9",
    val deviceBattery: Int = 85,
    val isDeviceConnected: Boolean = true,
    val phaseSoundEnabled: Boolean = true,
    val signalVolume: Float = 0.6f,
    val tags: List<WorkoutTag> = listOf(
        WorkoutTag(UUID.randomUUID().toString(), "Общая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Силовая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Легкая", "#6750A4", false),
        WorkoutTag(UUID.randomUUID().toString(), "Вело", "#6750A4", false)
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