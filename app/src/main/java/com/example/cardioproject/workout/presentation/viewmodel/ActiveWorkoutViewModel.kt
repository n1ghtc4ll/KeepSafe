package com.example.cardioproject.workout.presentation.viewmodel

import android.media.AudioManager
import android.media.ToneGenerator
import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.core.bluetooth.data.BleConnectionManager
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
import kotlin.time.Duration.Companion.milliseconds

class ActiveWorkoutViewModel(
    private val startTimerUseCase: StartWorkoutTimerUseCase,
    private val workoutRepository: WorkoutSessionRepository,
    private val settingsRepository: SettingsRepository,
    private val bleConnectionManager: BleConnectionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveWorkoutUiState())
    val uiState = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var heartRateJob: Job? = null
    private val heartRateHistory = mutableListOf<Int>()
    private var currentSettings: WorkoutSettings? = null
    private var sessionStartTime: Long = 0

    private var toneGenerator: ToneGenerator? = null
    private var isPhaseSoundEnabled = true
    private var isCriticalPulseAlertEnabled = true
    private var isCurrentlyInCriticalZone = false
    private var signalVolume = 0.6f

    fun startWorkout(settings: WorkoutSettings) {
        if (heartRateJob?.isActive == true) return

        currentSettings = settings
        sessionStartTime = System.currentTimeMillis()

        viewModelScope.launch {
            settingsRepository.observeSettings().collect { trainingSettings ->
                isPhaseSoundEnabled = trainingSettings.phaseSoundEnabled
                isCriticalPulseAlertEnabled = trainingSettings.criticalPulseAlertEnabled
                
                if (signalVolume != trainingSettings.signalVolume || toneGenerator == null) {
                    signalVolume = trainingSettings.signalVolume
                    val volumeInt = (signalVolume * 100).toInt().coerceIn(0, 100)
                    toneGenerator?.release()
                    toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, volumeInt)
                }

                _uiState.update { it.copy(
                    hrZones = trainingSettings.heartRateZones,
                    keepScreenOn = trainingSettings.keepScreenOnEnabled
                ) }
            }
        }

        heartRateJob = viewModelScope.launch {
            bleConnectionManager.heartRateBpm.collect { bpm ->
                if (bpm > 0) {
                    onNewHeartRateReceived(bpm)
                }
            }
        }

        if (settings.isTabataEnabled && settings.tabataSetup != null) {
            timerJob = viewModelScope.launch {
                var previousPhase: WorkoutPhase? = null
                
                startTimerUseCase(settings.tabataSetup).collect { timerState ->
                    if (previousPhase != null && previousPhase != timerState.currentPhase && timerState.currentPhase != WorkoutPhase.FINISHED) {
                        playPhaseChangeSound()
                    }
                    previousPhase = timerState.currentPhase

                    _uiState.update { it.copy(
                        currentPhase = timerState.currentPhase,
                        tabataTimerRemainingSec = timerState.timeRemainingSec,
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
                    delay(1000.milliseconds)
                    elapsed++
                    _uiState.update { it.copy(
                        currentPhase = WorkoutPhase.WORKOUT,
                        totalElapsedTimeSec = elapsed,
                        tabataTimerRemainingSec = elapsed
                    )}
                }
            }
        }
    }


    private fun playPhaseChangeSound() {
        if (isPhaseSoundEnabled) {
            toneGenerator?.startTone(ToneGenerator.TONE_CDMA_PIP, 300)
        }
    }

    fun onNewHeartRateReceived(bpm: Int) {
        heartRateHistory.add(bpm)
        
        val zones = _uiState.value.hrZones
        if (zones.isNotEmpty() && isCriticalPulseAlertEnabled) {
            val criticalZoneThreshold = zones.last().lowerBpm
            val isNowCritical = bpm >= criticalZoneThreshold
            
            if (isNowCritical && !isCurrentlyInCriticalZone) {
                toneGenerator?.startTone(ToneGenerator.TONE_CDMA_HIGH_L, 500)
            }
            isCurrentlyInCriticalZone = isNowCritical
        }
        
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

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onCleared() {
        toneGenerator?.release()
        //bleConnectionManager.disconnect()
    }
}