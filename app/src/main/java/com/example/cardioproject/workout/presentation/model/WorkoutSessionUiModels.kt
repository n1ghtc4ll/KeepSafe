package com.example.cardioproject.workout.presentation.model

import com.example.cardioproject.core.common.presentation.model.WorkoutTagUi

data class WorkoutSessionUi(
    val id: String,
    var title: String,
    val date: String,
    val time: String,
    val duration: String,
    var tag: WorkoutTagUi,
    var isSelected: Boolean = false
)