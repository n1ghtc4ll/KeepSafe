package com.example.cardioproject.settings.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.settings.presentation.model.PhaseType
import com.example.cardioproject.settings.presentation.model.ProfileBuilderUiState
import com.example.cardioproject.workout.domain.model.TabataSetup
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
                            tabataSetup = it.tabataSetup.copy(
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
            val tabataSetup = currentState.tabataSetup

            when (paramName) {
                "warmUp" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(warmUpTimeSec = value)
                )
                "reps" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(repsCount = value)
                )
                "workout" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(workoutTimeSec = value)
                )
                "relax" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(relaxTimeSec = value)
                )
                "break" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(breakTimeSec = value)
                )
                "coolDown" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(coolDownTimeSec = value)
                )
                "sets" -> currentState.copy(
                    tabataSetup = tabataSetup.copy(setsCount = value)
                )
                else -> currentState
            }
        }
    }

    fun onPhaseColorChange(phase: PhaseType, colorHex: Long) {
        _uiState.update { currentState ->
            val tabataSetup = currentState.tabataSetup

            when (phase) {
                PhaseType.WARM_UP -> currentState.copy(
                    tabataSetup = tabataSetup.copy(warmUpColorHex = colorHex)
                )
                PhaseType.WORKOUT -> currentState.copy(
                    tabataSetup = tabataSetup.copy(workoutColorHex = colorHex)
                )
                PhaseType.RELAX -> currentState.copy(
                    tabataSetup = tabataSetup.copy(relaxColorHex = colorHex)
                )
                PhaseType.BREAK -> currentState.copy(
                    tabataSetup = tabataSetup.copy(breakColorHex = colorHex)
                )
                PhaseType.COOL_DOWN -> currentState.copy(
                    tabataSetup = tabataSetup.copy(coolDownColorHex = colorHex)
                )
            }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val finalName = currentState.name.ifBlank { "Мой таймер" }
        val tabataSetup = currentState.tabataSetup

        val profileToSave = TabataProfile(
            id = currentState.id ?: UUID.randomUUID().toString(),
            name = finalName,
            setsCount = tabataSetup.setsCount,
            repsCount = tabataSetup.repsCount,
            warmUpTimeSec = tabataSetup.warmUpTimeSec,
            warmUpColorHex = tabataSetup.warmUpColorHex,
            workoutTimeSec = tabataSetup.workoutTimeSec,
            workoutColorHex = tabataSetup.workoutColorHex,
            relaxTimeSec = tabataSetup.relaxTimeSec,
            relaxColorHex = tabataSetup.relaxColorHex,
            breakTimeSec = tabataSetup.breakTimeSec,
            breakColorHex = tabataSetup.breakColorHex,
            coolDownTimeSec = tabataSetup.coolDownTimeSec,
            coolDownColorHex = tabataSetup.coolDownColorHex
        )

        viewModelScope.launch {
            repository.saveTabataProfile(profileToSave)
            onSuccess()
        }
    }
}