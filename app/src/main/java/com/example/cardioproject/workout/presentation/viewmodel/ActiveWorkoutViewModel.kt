package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.bluetooth.domain.HeartRateDevice
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import com.example.cardioproject.workout.domain.model.WorkoutPhase
import com.example.cardioproject.workout.domain.model.WorkoutSession
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import com.example.cardioproject.workout.domain.usecase.StartWorkoutTimerUseCase
import com.example.cardioproject.workout.presentation.model.ActiveWorkoutUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class ActiveWorkoutViewModel(
    private val startTimerUseCase: StartWorkoutTimerUseCase,
    private val workoutRepository: WorkoutSessionRepository,
    private val settingsRepository: SettingsRepository,
    private val heartRateDevice: HeartRateDevice
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var heartRateJob: Job? = null
    private val heartRateHistory = mutableListOf<Int>()
    private var currentSettings: WorkoutSettings? = null
    private var sessionStartTime: Long = 0

    init {
        viewModelScope.launch {
            heartRateDevice.heartRate.collect { bpm ->
                if (bpm > 0) {
                    onNewHeartRateReceived(bpm)
                }
            }
        }
    }

    fun startWorkout(settings: WorkoutSettings) {
        currentSettings = settings
        sessionStartTime = System.currentTimeMillis()

        viewModelScope.launch {
            settingsRepository.observeSettings().collect { trainingSettings ->
                _uiState.update { it.copy(hrZones = trainingSettings.heartRateZones) }
            }
        }

        startHeartRateSimulation()

        if (settings.isTabataEnabled && settings.tabataSetup != null) {
            timerJob = viewModelScope.launch {
                startTimerUseCase(settings.tabataSetup).collect { timerState ->
                    _uiState.update { it.copy(
                        currentPhase = timerState.currentPhase,
                        timeRemainingSec = timerState.timeRemainingSec,
                        currentSet = timerState.currentSet,
                        currentRep = timerState.currentRep,
                        totalElapsedTimeSec = timerState.totalElapsedTimeSec
                    )}

                    if (timerState.currentPhase == WorkoutPhase.FINISHED) {
                        finishWorkout()
                    }
                }
            }
        } else {
            timerJob = viewModelScope.launch {
                var elapsed = 0
                while (true) {
                    delay(1000)
                    elapsed++
                    _uiState.update { it.copy(
                        currentPhase = WorkoutPhase.WORKOUT,
                        totalElapsedTimeSec = elapsed,
                        timeRemainingSec = elapsed
                    )}
                }
            }
        }
    }

    // !!!! Имитация пульса для теста до интеграции с BT !!!!
    private fun startHeartRateSimulation() {
        heartRateJob = viewModelScope.launch {
            while (true) {
                val fakeBpm = Random.nextInt(110, 165)
                // Сохраняем в локальный список для базы данных
                heartRateHistory.add(fakeBpm)
                // ОБЯЗАТЕЛЬНО обновляем список внутри ActiveWorkoutUiState
                _uiState.update { state ->
                    state.copy(
                        currentHeartRate = fakeBpm,
                        heartRateHistory = state.heartRateHistory + fakeBpm
                    )
                }
                delay(1000)
            }
        }
    }

    fun onNewHeartRateReceived(bpm: Int) {
        heartRateHistory.add(bpm)
        _uiState.update { state ->
            state.copy(
                currentHeartRate = bpm,
                heartRateHistory = state.heartRateHistory + bpm
            )
        }
    }

    fun stopWorkout() {
        finishWorkout()
    }

    private fun finishWorkout() {
        timerJob?.cancel()
        heartRateJob?.cancel()
        _uiState.update { it.copy(currentPhase = WorkoutPhase.FINISHED) }
        saveSessionToDatabase()
    }

    private fun saveSessionToDatabase() {
        val settings = currentSettings ?: return
        val finalState = _uiState.value

        viewModelScope.launch {
            val session = WorkoutSession(
                id = UUID.randomUUID().toString(),
                dateTimestamp = sessionStartTime,
                durationSec = finalState.totalElapsedTimeSec,
                tag = settings.selectedTag,
                averageHeartRate = if (heartRateHistory.isNotEmpty()) heartRateHistory.average()
                    .toInt() else 0,
                maxHeartRate = heartRateHistory.maxOrNull(),
                minHeartRate = heartRateHistory.minOrNull(),
                heartRateHistory = heartRateHistory.toList()
            )
            workoutRepository.saveWorkoutResult(session)
        }
    }

    override fun onCleared() {
        super.onCleared()
        heartRateDevice.disconnect()
    }
}