package com.example.cardioproject.workout.domain.repository

import com.example.cardioproject.workout.domain.model.WorkoutSession
import kotlinx.coroutines.flow.Flow

interface WorkoutSessionRepository {
    suspend fun saveWorkoutResult(session: WorkoutSession)

    fun getWorkoutHistory(): Flow<List<WorkoutSession>>
}