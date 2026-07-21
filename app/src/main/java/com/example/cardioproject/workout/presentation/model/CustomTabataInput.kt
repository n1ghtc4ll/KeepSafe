package com.example.cardioproject.workout.presentation.model

data class CustomTabataInput(
    val setsCount: String = "1",
    val repsCount: String = "4",
    val warmUpMin: String = "0",
    val warmUpSec: String = "10",
    val workoutMin: String = "0",
    val workoutSec: String = "20",
    val relaxMin: String = "0",
    val relaxSec: String = "10",
    val breakMin: String = "0",
    val breakSec: String = "0",
    val coolDownMin: String = "0",
    val coolDownSec: String = "10"
)
