package com.example.cardioproject.workout.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.workout.presentation.model.WorkoutSessionUi
import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi
import com.example.cardioproject.core.common.presentation.model.toUiTag
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import com.example.cardioproject.core.common.presentation.model.toDomain
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutDetailsViewModel(
    private val workoutId: String,
    private val repository: WorkoutSessionRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val workout = repository.getWorkoutById(workoutId)
        .map { domainSession ->
            if (domainSession == null) return@map null

            val dateObj = Date(domainSession.dateTimestamp)
            val h = domainSession.durationSec / 3600
            val m = (domainSession.durationSec % 3600) / 60
            val s = domainSession.durationSec % 60
            val durationString = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)

            val uiTag = domainSession.tag?.toUiTag(false)
                ?: WorkoutTagUi("0", "Без тега", "#808080", false)

            WorkoutSessionUi(
                id = domainSession.id,
                title = uiTag.name, // Используем имя тега вместо отсутствующего title
                date = dateFormat.format(dateObj),
                time = timeFormat.format(dateObj),
                duration = durationString,
                tag = uiTag,
                isSelected = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun updateTag(newTag: WorkoutTagUi) {
        viewModelScope.launch {
            val domainSession = repository.getWorkoutById(workoutId).firstOrNull()
            if (domainSession != null) {
                val updatedSession = domainSession.copy(tag = newTag.toDomain())
                repository.updateWorkout(updatedSession)
            }
        }
    }
}