package com.example.cardioproject.workout.data.repository

import com.example.cardioproject.core.common.data.dao.TagDao
import com.example.cardioproject.core.common.data.mapper.toDomain
import com.example.cardioproject.workout.data.dao.WorkoutSessionDao
import com.example.cardioproject.workout.data.mapper.toDomain
import com.example.cardioproject.workout.data.mapper.toEntity
import com.example.cardioproject.workout.domain.model.WorkoutSession
import com.example.cardioproject.workout.domain.repository.WorkoutSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkoutSessionRepositoryImpl(
    private val workoutDao: WorkoutSessionDao,
    private val tagDao: TagDao
): WorkoutSessionRepository {
    override suspend fun saveWorkoutResult(session: WorkoutSession) {
        workoutDao.insertWorkout(session.toEntity())
    }

    override fun getWorkoutHistory(): Flow<List<WorkoutSession>> {
        return workoutDao.getAllWorkouts().map { workoutEntityList ->
            workoutEntityList.map { workoutEntity ->
                val domainTag = workoutEntity.tagId?.let { id -> tagDao.getTagById(id)?.toDomain() }

                workoutEntity.toDomain(domainTag)
            }
        }
    }
}