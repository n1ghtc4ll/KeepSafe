package com.example.cardioproject.workout.presentation.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardioproject.workout.presentation.screen.WorkoutSession
import com.example.cardioproject.workout.presentation.screen.WorkoutTag
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutHistoryViewModel(
    private val repository: WorkoutSessionRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val workouts = repository.getWorkoutHistory()
        .map { domainList ->
            domainList.map { domainSession ->
                val dateObj = Date(domainSession.dateTimestamp)

                val h = domainSession.durationSec / 3600
                val m = (domainSession.durationSec % 3600) / 60
                val s = domainSession.durationSec % 60
                val durationString = String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)

                val uiTag = domainSession.tag?.let { tag ->
                    WorkoutTag(tag.id, tag.name, Color.Red)
                } ?: WorkoutTag("0", "Без тега", Color.Gray)

                WorkoutSession(
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

    // TODO: Здесь тоже нужно будет делать обратный маппинг UI-модели в Domain-модель перед сохранением
}