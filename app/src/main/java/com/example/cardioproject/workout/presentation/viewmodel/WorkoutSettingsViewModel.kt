package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.common.domain.model.TabataProfile
import com.example.cardioproject.core.common.domain.model.Tag
import com.example.cardioproject.core.navigation.Route
import com.example.cardioproject.core.navigation.TopLevelBackStack
import com.example.cardioproject.workout.domain.model.TabataSetup
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.domain.usecase.GetWorkoutSetupDataUseCase
import com.example.cardioproject.workout.presentation.model.WorkoutSettingsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

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
                    currentState.copy(
                        availableTags = setupData.tags,
                        availableProfiles = setupData.tabataProfiles,
                        selectedProfile = currentState.selectedProfile ?: setupData.tabataProfiles.firstOrNull()
                    )
                }
            }
        }
    }

    fun onStartClick() {
        //topLevelBackStack.add()
    }

    fun onTitleChanged(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onTagSelected(tag: Tag) {
        _uiState.update { it.copy(selectedTag = tag) }
    }

    fun onTabataToggled(isEnabled: Boolean) {
        _uiState.update { it.copy(isTabataEnabled = isEnabled) }
    }

    fun onProfileSelected(profile: TabataProfile) {
        _uiState.update { it.copy(selectedProfile = profile) }
    }

    fun getFinalSettings(): WorkoutSettings {
        val state = _uiState.value
        val tabataSetup = if (state.isTabataEnabled && state.selectedProfile != null) {
            TabataSetup.Preset(state.selectedProfile)
        } else null

        return WorkoutSettings(
            title = state.title.ifBlank { "Тренировка" },
            selectedTag = state.selectedTag,
            isTabataEnabled = state.isTabataEnabled,
            tabataSetup = tabataSetup
        )
    }
}