package com.example.cardioproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cardioproject.presentation.HeartRateMainScreen
import com.example.cardioproject.presentation.WorkoutDetailsScreen
import com.example.cardioproject.presentation.WorkoutHistoryScreen
import com.example.cardioproject.presentation.WorkoutSession
import com.example.cardioproject.presentation.WorkoutSettingsScreen
import com.example.cardioproject.presentation.defaultTags
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // Используем стандартную тему оформления проекта
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Запускаем наш менеджер переключения экранов
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * Этот компонент отвечает за то, какой экран показывать в данный момент.
 */
@Composable
fun AppNavigation() {
    // Состояние, хранящее имя текущего экрана
    var currentScreen by remember { mutableStateOf("MAIN") }

    // Храним ID выбранной тренировки для экрана деталей
    var selectedWorkoutId by remember { mutableStateOf<String?>(null) }

    // Глобальное хранилище тренировок (пока без БД)
    val globalWorkouts = remember {
        mutableStateListOf(
            WorkoutSession(
                UUID.randomUUID().toString(),
                "Интервальная тренировка",
                "12.07.2023",
                "08:00",
                "00:45:00",
                defaultTags[0]
            ),
            WorkoutSession(
                UUID.randomUUID().toString(),
                "Силовая тренировка",
                "10.07.2023",
                "18:30",
                "01:20:00",
                defaultTags[1]
            ),
            WorkoutSession(
                UUID.randomUUID().toString(),
                "Легкая пробежка",
                "08.07.2023",
                "07:00",
                "00:30:00",
                defaultTags[2]
            ),
            WorkoutSession(
                UUID.randomUUID().toString(),
                "Табата интенсив",
                "05.07.2023",
                "19:00",
                "00:20:00",
                defaultTags[0]
            )
        )
    }

    when (currentScreen) {
        "MAIN" -> {
            HeartRateMainScreen(
                onStartWorkoutClick = { currentScreen = "SETTINGS" },
                onAddMeasurementsClick = { /* TODO для других участников */ },
                onSettingsClick = { /* TODO настройки приложения/устройства */ }
            )
        }
        "SETTINGS" -> {
            WorkoutSettingsScreen(
                onBackClick = { currentScreen = "MAIN" },
                onStartClick = { newWorkout ->
                    // Добавляем созданную в настройках тренировку в историю
                    globalWorkouts.add(0, newWorkout)
                    currentScreen = "HISTORY"
                }
            )
        }

        "HISTORY" -> {
            WorkoutHistoryScreen(
                workouts = globalWorkouts,
                onBackClick = { currentScreen = "MAIN" },
                onWorkoutClick = { workoutId ->
                    // Сохраняем ID и переходим на детали
                    selectedWorkoutId = workoutId
                    currentScreen = "DETAILS"
                },
                onAddWorkout = { newWorkout ->
                    globalWorkouts.add(0, newWorkout)
                },
                onEditWorkout = { updatedWorkout ->
                    val index = globalWorkouts.indexOfFirst { it.id == updatedWorkout.id }
                    if (index != -1) {
                        globalWorkouts[index] = updatedWorkout
                    }
                },
                onDeleteWorkout = { workoutToDelete ->
                    globalWorkouts.remove(workoutToDelete)
                }
            )
        }

        "DETAILS" -> {
            // Ищем тренировку по сохраненному ID
            val selectedWorkout = globalWorkouts.find { it.id == selectedWorkoutId }

            if (selectedWorkout != null) {
                WorkoutDetailsScreen(
                    workout = selectedWorkout,
                    onBackClick = { currentScreen = "HISTORY" },
                    onWorkoutUpdated = { updatedWorkout ->
                        // Обновляем тренировку в глобальном списке при редактировании на экране деталей
                        val index = globalWorkouts.indexOfFirst { it.id == updatedWorkout.id }
                        if (index != -1) {
                            globalWorkouts[index] = updatedWorkout
                        }
                    }
                )
            } else {
                // Если по какой-то причине тренировка не найдена (например, удалили), возвращаемся назад
                currentScreen = "HISTORY"
            }
        }
    }
}