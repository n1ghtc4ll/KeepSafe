package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.domain.usecase.GetWorkoutSetupDataUseCase
import com.example.cardioproject.workout.presentation.model.WorkoutSettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WorkoutSettingsViewModel(
    private val getWorkoutSetupDataUseCase: GetWorkoutSetupDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutSettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadSetupData()
    }

    private fun loadSetupData() {
        viewModelScope.launch {
            getWorkoutSetupDataUseCase().collect { setupData ->
                _uiState.update { currentState ->
                    val defaultSetup = setupData.tabataProfiles.firstOrNull()?.let {
                        TabataSetup.Preset(it)
                    } ?: TabataSetup.Custom()

                    currentState.copy(
                        availableTags = setupData.tags,
                        availableProfiles = setupData.tabataProfiles,
                        tabataSetup = currentState.tabataSetup ?: defaultSetup
                    )
                }
            }
        }
    }

    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onTagSelected(tag: Tag?) {
        _uiState.update { it.copy(selectedTag = tag) }
    }

    fun onTabataToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(isTabataEnabled = isEnabled) }
    }

    fun onProfileSelected(profile: TabataProfile?) {
        _uiState.update { state ->
            if (profile != null) {
                state.copy(tabataSetup = TabataSetup.Preset(profile))
            }
            else {
                state.copy(tabataSetup =
                    TabataSetup.Custom(
                        setsCount = 0,
                        repsCount = 0,
                        warmUpTimeSec = 0,
                        workoutTimeSec = 0,
                        relaxTimeSec = 0,
                        breakTimeSec = 0,
                        coolDownTimeSec = 0
                    )
                )
            }
        }
    }

    fun onCustomParamChanged(paramType: String, newValue: Int) {
        _uiState.update { state ->
            val currentSetup = state.tabataSetup
            if (currentSetup !is TabataSetup.Custom) return@update state

            val updatedSetup = when (paramType) {
                "warmUp" -> currentSetup.copy(warmUpTimeSec = newValue)
                "reps" -> currentSetup.copy(repsCount = newValue)
                "workout" -> currentSetup.copy(workoutTimeSec = newValue)
                "relax" -> currentSetup.copy(relaxTimeSec = newValue)
                "coolDown" -> currentSetup.copy(coolDownTimeSec = newValue)
                "sets" -> currentSetup.copy(setsCount = newValue)
                "break" -> currentSetup.copy(breakTimeSec = newValue)
                else -> currentSetup
            }
            state.copy(tabataSetup = updatedSetup)
        }
    }

    fun getFinalSettings(): WorkoutSettings {
        val state = _uiState.value
        return WorkoutSettings(
            title = state.title.ifBlank { "Тренировка" },
            selectedTag = state.selectedTag,
            isTabataEnabled = state.isTabataEnabled,
            tabataSetup = if (state.isTabataEnabled) state.tabataSetup else null
        )
    }
}