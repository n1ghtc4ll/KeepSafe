package com.example.cardioproject.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.settings.domain.model.HeartRateZone as DomainHeartRateZone
import com.example.cardioproject.settings.domain.model.TrainingSettings
import com.example.cardioproject.settings.domain.repository.SettingsRepository
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
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

    private var latestSettings: TrainingSettings = TrainingSettings()

    init {
        // 1. Подписываемся на общие настройки (DataStore)
        viewModelScope.launch {
            observeSettings().collect { settings ->
                latestSettings = settings
                _uiState.update { currentState ->
                    currentState.copy(
                        deviceName = settings.device.name,
                        deviceBattery = settings.device.batteryLevel,
                        isDeviceConnected = settings.device.isConnected,
                        phaseSoundEnabled = settings.phaseSoundEnabled,
                        signalVolume = settings.signalVolume,
                        birthDate = settings.profile.birthDate,
                        gender = settings.profile.gender,
                        height = settings.profile.heightCm?.toString().orEmpty(),
                        zones = settings.heartRateZones.map { it.toUiZone() },
                        criticalPulseAlertEnabled = settings.criticalPulseAlertEnabled,
                        keepScreenOnEnabled = settings.keepScreenOnEnabled
                    )
                }
            }
        }

        // 2. Подписываемся на профили Табаты (Room)
        viewModelScope.launch {
            settingsRepository.getTabataProfiles().collect { profiles ->
                _uiState.update { it.copy(timerProfiles = profiles.map(TabataProfile::toUiProfile)) }
            }
        }

        // 3. Подписываемся на теги (Room)
        viewModelScope.launch {
            settingsRepository.getTags().collect { tags ->
                _uiState.update { it.copy(tags = tags.map(Tag::toUiTag)) }
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
        viewModelScope.launch {
            // Используем UseCase вместо репозитория
            addWorkoutTag(name, "#6750A4")
        }
    }

    fun onEditTag(tag: WorkoutTag, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            val updatedTag = Tag(
                id = tag.id,
                name = newName,
                color = tag.color
            )
            // Используем UseCase
            editWorkoutTag(updatedTag)
        }
    }

    fun onDeleteTag(tag: WorkoutTag) {
        viewModelScope.launch {
            // Используем UseCase
            removeWorkoutTag(tag.id)
        }
    }

    fun onDeleteProfile(profile: TimerProfileUi) {
        viewModelScope.launch {
            settingsRepository.deleteTabataProfile(profile.id)
        }
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
        viewModelScope.launch {
            autoCalculateHeartRateZones(
                latestSettings.heartRateZones,
                birthDateStr = uiState.value.birthDate
            )
        }
    }

    fun onCriticalPulseAlertToggle(enabled: Boolean) {
        viewModelScope.launch { updateCriticalPulseAlert(enabled) }
    }

    fun onKeepScreenOnToggle(enabled: Boolean) {
        viewModelScope.launch { updateKeepScreenOn(enabled) }
    }
}

// ---------- Mapping: domain -> presentation ----------

private fun DomainHeartRateZone.toUiZone() = HeartRateZone(
    label = label,
    range = lowerBpm.toFloat()..upperBpm.toFloat()
)

private fun TabataProfile.toUiProfile() = TimerProfileUi(
    id = this.id,
    name = this.name
)

private fun Tag.toUiTag() = WorkoutTag(
    id = this.id,
    name = this.name,
    color = this.color,
    editable = true
)