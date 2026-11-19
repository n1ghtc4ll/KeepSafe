package com.example.cardioproject.workout.presentation.model

import androidx.compose.ui.graphics.Color

data class WorkoutTagUi(
    val id: String,
    var name: String,
    var color: Color
)

data class WorkoutSessionUi(
    val id: String,
    var title: String,
    val date: String,
    val time: String,
    val duration: String,
    var tag: WorkoutTagUi,
    var isSelected: Boolean = false
)