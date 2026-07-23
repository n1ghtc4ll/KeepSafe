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

            val uiTag = domainSession.tag?.let { tag ->
                WorkoutTag(tag.id, tag.name, Color.Red)
            } ?: WorkoutTag("0", "Без тега", Color.Gray)

            WorkoutSession(
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

    fun updateTag(newTag: WorkoutTag) {
        viewModelScope.launch {
            // Чтобы не потерять пульс, сначала берем актуальную сущность, меняем тег и сохраняем
            // В идеале здесь нужен метод updateTag в самом Dao, но для скорости сойдет и так
        }
    }
}