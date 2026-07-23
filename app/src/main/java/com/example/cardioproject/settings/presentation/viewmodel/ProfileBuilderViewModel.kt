package com.example.cardioproject.settings.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.settings.presentation.screen.PhaseType
import com.example.cardioproject.settings.presentation.screen.ProfileBuilderUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class ProfileBuilderViewModel(
    private val profileId: String?,
    private val repository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileBuilderUiState(id = profileId))
    val uiState: StateFlow<ProfileBuilderUiState> = _uiState.asStateFlow()

    init {
        profileId?.let { id ->
            viewModelScope.launch {
                repository.getTabataProfileById(id)?.let { profile ->
                    _uiState.update {
                        it.copy(
                            id = profile.id,
                            name = profile.name,
                            setsCount = profile.setsCount,
                            repsCount = profile.repsCount,
                            warmUpTimeSec = profile.warmUpTimeSec,
                            warmUpColorHex = profile.warmUpColorHex,
                            workoutTimeSec = profile.workoutTimeSec,
                            workoutColorHex = profile.workoutColorHex,
                            relaxTimeSec = profile.relaxTimeSec,
                            relaxColorHex = profile.relaxColorHex,
                            breakTimeSec = profile.breakTimeSec,
                            breakColorHex = profile.breakColorHex,
                            coolDownTimeSec = profile.coolDownTimeSec,
                            coolDownColorHex = profile.coolDownColorHex
                        )
                    }
                }
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onParamChange(paramName: String, value: Int) {
        _uiState.update { currentState ->
            when (paramName) {
                "warmUp" -> currentState.copy(warmUpTimeSec = value)
                "reps" -> currentState.copy(repsCount = value)
                "workout" -> currentState.copy(workoutTimeSec = value)
                "relax" -> currentState.copy(relaxTimeSec = value)
                "break" -> currentState.copy(breakTimeSec = value)
                "coolDown" -> currentState.copy(coolDownTimeSec = value)
                "sets" -> currentState.copy(setsCount = value)
                else -> currentState
            }
        }
    }

    fun onPhaseColorChange(phase: PhaseType, colorHex: Long) {
        _uiState.update { currentState ->
            when (phase) {
                PhaseType.WARM_UP -> currentState.copy(warmUpColorHex = colorHex)
                PhaseType.WORKOUT -> currentState.copy(workoutColorHex = colorHex)
                PhaseType.RELAX -> currentState.copy(relaxColorHex = colorHex)
                PhaseType.BREAK -> currentState.copy(breakColorHex = colorHex)
                PhaseType.COOL_DOWN -> currentState.copy(coolDownColorHex = colorHex)
            }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val finalName = currentState.name.ifBlank { "Мой таймер" }

        val profileToSave = TabataProfile(
            id = currentState.id ?: UUID.randomUUID().toString(),
            name = finalName,
            setsCount = currentState.setsCount,
            repsCount = currentState.repsCount,
            warmUpTimeSec = currentState.warmUpTimeSec,
            warmUpColorHex = currentState.warmUpColorHex,
            workoutTimeSec = currentState.workoutTimeSec,
            workoutColorHex = currentState.workoutColorHex,
            relaxTimeSec = currentState.relaxTimeSec,
            relaxColorHex = currentState.relaxColorHex,
            breakTimeSec = currentState.breakTimeSec,
            breakColorHex = currentState.breakColorHex,
            coolDownTimeSec = currentState.coolDownTimeSec,
            coolDownColorHex = currentState.coolDownColorHex
        )

        viewModelScope.launch {
            repository.saveTabataProfile(profileToSave)
            onSuccess()
        }
    }
}