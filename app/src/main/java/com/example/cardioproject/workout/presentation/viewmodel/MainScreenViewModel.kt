package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.bluetooth.data.BleConnectionManager
import com.example.cardioproject.core.bluetooth.domain.ConnectionState
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.workout.presentation.model.MainScreenUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class MainScreenViewModel(
    private val bleConnectionManager: BleConnectionManager,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState = combine(
        bleConnectionManager.heartRateBpm.onStart { emit(0) },
        bleConnectionManager.connectionState,
        settingsRepository.observeSettings()
    ) { bpm, connectionState, settings ->
        val isConnected = connectionState is ConnectionState.Connected

        MainScreenUiState(
            currentBpm = if (isConnected) bpm else null,
            deviceName = settings.device.name.ifBlank { null },
            deviceBattery = settings.device.batteryLevel,
            isDeviceConnected = isConnected
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainScreenUiState()
    )
}