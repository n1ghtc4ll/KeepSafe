package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.workout.presentation.model.WorkoutSessionUi
import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi
import com.example.cardioproject.core.common.presentation.model.toDomain
import com.example.cardioproject.core.common.presentation.model.toUiTag
import com.example.cardioproject.workout.domain.model.WorkoutSession
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import com.example.cardioproject.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutHistoryViewModel(
    private val repository: WorkoutSessionRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val availableTags = settingsRepository.getTags()
        .map { tags -> tags.map { it.toUiTag(editable = false) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workouts = repository.getWorkoutHistory()
        .map { domainList ->
            domainList.map { domainSession ->
                val dateObj = Date(domainSession.dateTimestamp)

                val h = domainSession.durationSec / 3600
                val m = (domainSession.durationSec % 3600) / 60
                val s = domainSession.durationSec % 60
                val durationString = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)

                val uiTag = domainSession.tag?.toUiTag(false)
                    ?: WorkoutTagUi("0", "Без тега", "#808080", false)

                WorkoutSessionUi(
                    id = domainSession.id,
                    title = "Тренировка",
                    date = dateFormat.format(dateObj),
                    time = timeFormat.format(dateObj),
                    duration = durationString,
                    tag = uiTag,
                    isSelected = false
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun saveWorkout(sessionUi: WorkoutSessionUi) {
        viewModelScope.launch {
            val dateObj = dateFormat.parse(sessionUi.date)
            val timeParts = sessionUi.time.split(":")
            val h = timeParts.getOrNull(0)?.toIntOrNull() ?: 0
            val m = timeParts.getOrNull(1)?.toIntOrNull() ?: 0
            
            val timestamp = if (dateObj != null) {
                dateObj.time + (h * 3600 + m * 60) * 1000L
            } else {
                System.currentTimeMillis()
            }
            
            val durationParts = sessionUi.duration.split(":")
            val durH = durationParts.getOrNull(0)?.toIntOrNull() ?: 0
            val durM = durationParts.getOrNull(1)?.toIntOrNull() ?: 0
            val durS = durationParts.getOrNull(2)?.toIntOrNull() ?: 0
            val durationSec = durH * 3600 + durM * 60 + durS
            
            val session = WorkoutSession(
                id = sessionUi.id,
                dateTimestamp = timestamp,
                durationSec = durationSec,
                tag = sessionUi.tag.toDomain(),
                averageHeartRate = 0,
                maxHeartRate = null,
                minHeartRate = null,
                heartRateHistory = emptyList()
            )
            
            repository.updateWorkout(session)
        }
    }

    fun deleteWorkout(sessionUi: WorkoutSessionUi) {
        viewModelScope.launch {
            val session = WorkoutSession(
                id = sessionUi.id,
                dateTimestamp = 0, // Не важно для удаления
                durationSec = 0,
                tag = null,
                averageHeartRate = 0,
                maxHeartRate = null,
                minHeartRate = null,
                heartRateHistory = emptyList()
            )
            repository.deleteWorkout(session)
        }
    }

}