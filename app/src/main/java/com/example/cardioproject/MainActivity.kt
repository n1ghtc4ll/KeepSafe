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
import com.example.cardioproject.workout.presentation.screen.WorkoutSettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}


@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("MAIN") }

    when (currentScreen) {
        "MAIN" -> {
            HeartRateMainScreen(
                onStartWorkoutClick = { currentScreen = "SETTINGS" }
            )
        }

        "SETTINGS" -> {
            WorkoutSettingsScreen(
                onBackClick = { currentScreen = "MAIN" },
                onStartClick = { currentScreen = "MAIN" }
            )
        }
    }
}
