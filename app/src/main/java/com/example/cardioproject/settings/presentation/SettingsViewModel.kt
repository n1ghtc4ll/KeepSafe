package com.example.cardioproject.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.settings.domain.model.HeartRateZone as DomainHeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.model.WorkoutTag as DomainWorkoutTag
import com.example.cardioproject.settings.domain.usecase.AddWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.AutoCalculateHeartRateZonesUseCase
import com.example.cardioproject.settings.domain.usecase.EditWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.ObserveSettingsUseCase
import com.example.cardioproject.settings.domain.usecase.RemoveWorkoutTagUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateCriticalPulseAlertUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateHeartRateZoneUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateKeepScreenOnUseCase
import com.example.cardioproject.settings.domain.usecase.UpdatePhaseSoundUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateSignalVolumeUseCase
import com.example.cardioproject.settings.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Связывает domain-слой (usecase'ы) с презентационным SettingsUiState.
 *
 * ВАЖНО: WorkoutTag и HeartRateZone существуют в двух версиях — доменной
 * (com.example.cardioproject.settings.domain.model) и презентационной
 * (com.example.cardioproject.settings.presentation, объявлена в SettingsScreen.kt).
 * Чтобы не путать их, доменные версии импортированы с алиасом (DomainWorkoutTag,
 * DomainHeartRateZone). Безымянные WorkoutTag / HeartRateZone в этом файле —
 * всегда презентационные типы, те же, что ожидает SettingsScreen.
 */
class SettingsViewModel(
    private val observeSettings: ObserveSettingsUseCase,
    private val updatePhaseSound: UpdatePhaseSoundUseCase,
    private val updateSignalVolume: UpdateSignalVolumeUseCase,
    private val addWorkoutTag: AddWorkoutTagUseCase,
    private val editWorkoutTag: EditWorkoutTagUseCase,
    private val removeWorkoutTag: RemoveWorkoutTagUseCase,
    private val updateUserProfile: UpdateUserProfileUseCase,
    private val updateHeartRateZone: UpdateHeartRateZoneUseCase,
    private val autoCalculateHeartRateZones: AutoCalculateHeartRateZonesUseCase,
    private val updateCriticalPulseAlert: UpdateCriticalPulseAlertUseCase,
    private val updateKeepScreenOn: UpdateKeepScreenOnUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    // Держим последний снимок domain-модели, чтобы не гонять usecase'ы за списком
    // тегов/зон каждый раз — обновления zones/tags идут по индексу/id из этого снимка.
    private var latestSettings: TrainingSettings = TrainingSettings()

    init {
        viewModelScope.launch {
            observeSettings().collect { settings ->
                latestSettings = settings
                _uiState.value = settings.toUiState()
            }
        }
    }

    fun onPhaseSoundToggle(enabled: Boolean) {
        viewModelScope.launch { updatePhaseSound(enabled) }
    }

    fun onVolumeChange(volume: Float) {
        viewModelScope.launch { updateSignalVolume(volume) }
    }

    fun onAddTag(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { addWorkoutTag(name) }
    }

    fun onEditTag(tag: WorkoutTag, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            editWorkoutTag(
                DomainWorkoutTag(
                    id = tag.id,
                    name = newName,
                    isEditable = tag.editable
                )
            )
        }
    }

    fun onDeleteTag(tag: WorkoutTag) {
        viewModelScope.launch { removeWorkoutTag(tag.id) }
    }

    fun onBirthDateChange(value: String) {
        viewModelScope.launch {
            updateUserProfile(latestSettings.profile.copy(birthDate = value))
        }
    }

    fun onGenderChange(value: String) {
        viewModelScope.launch {
            updateUserProfile(latestSettings.profile.copy(gender = value))
        }
    }

    fun onHeightChange(value: String) {
        val heightCm = value.toIntOrNull()
        viewModelScope.launch {
            updateUserProfile(latestSettings.profile.copy(heightCm = heightCm))
        }
    }

    fun onZoneChange(index: Int, range: ClosedFloatingPointRange<Float>) {
        viewModelScope.launch {
            updateHeartRateZone(
                currentZones = latestSettings.heartRateZones,
                index = index,
                lowerBpm = range.start.toInt(),
                upperBpm = range.endInclusive.toInt()
            )
        }
    }

    fun onAutoCalculateZones() {
        viewModelScope.launch { autoCalculateHeartRateZones(latestSettings.heartRateZones) }
    }

    fun onCriticalPulseAlertToggle(enabled: Boolean) {
        viewModelScope.launch { updateCriticalPulseAlert(enabled) }
    }

    fun onKeepScreenOnToggle(enabled: Boolean) {
        viewModelScope.launch { updateKeepScreenOn(enabled) }
    }
}

// ---------- Mapping: domain -> presentation ----------

private fun TrainingSettings.toUiState(): SettingsUiState = SettingsUiState(
    deviceName = device.name,
    deviceBattery = device.batteryLevel,
    isDeviceConnected = device.isConnected,
    phaseSoundEnabled = phaseSoundEnabled,
    signalVolume = signalVolume,
    tags = tags.map { it.toUiTag() },
    birthDate = profile.birthDate,
    gender = profile.gender,
    height = profile.heightCm?.toString().orEmpty(),
    zones = heartRateZones.map { it.toUiZone() },
    criticalPulseAlertEnabled = criticalPulseAlertEnabled,
    keepScreenOnEnabled = keepScreenOnEnabled
)

private fun DomainWorkoutTag.toUiTag() = WorkoutTag(
    id = id,
    name = name,
    editable = isEditable
)

private fun DomainHeartRateZone.toUiZone() = HeartRateZone(
    label = label,
    range = lowerBpm.toFloat()..upperBpm.toFloat()
)