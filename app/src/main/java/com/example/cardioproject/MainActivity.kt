package com.example.cardioproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cardioproject.presentation.HeartRateMainScreen
import com.example.cardioproject.presentation.WorkoutDetailsScreen
import com.example.cardioproject.presentation.WorkoutHistoryScreen
import com.example.cardioproject.presentation.WorkoutSettingsScreen

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

    when (currentScreen) {
        "MAIN" -> {
            HeartRateMainScreen(
                onStartWorkoutClick = { currentScreen = "SETTINGS" }
                // TODO: Позже можно добавить кнопку для перехода в историю
                // onHistoryClick = { currentScreen = "HISTORY" }
            )
        }
        "SETTINGS" -> {
            WorkoutSettingsScreen(
                onBackClick = { currentScreen = "MAIN" },
                onStartClick = { currentScreen = "HISTORY" } // Для теста переходим в Историю
            )
        }

        "HISTORY" -> {
            WorkoutHistoryScreen(
                onBackClick = { currentScreen = "MAIN" },
                onWorkoutClick = { workoutId ->
                    // При клике на карточку открываем детали
                    currentScreen = "DETAILS"
                }
            )
        }

        "DETAILS" -> {
            WorkoutDetailsScreen(
                onBackClick = { currentScreen = "HISTORY" }
            )
        }
    }
}
