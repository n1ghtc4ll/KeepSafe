package com.example.cardioproject.workout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cardioproject.workout.data.entity.WorkoutSessionDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutSessionDbEntity)

    @Query("SELECT * FROM workout_session_history ORDER BY dateTimestamp DESC")
    fun getAllWorkouts(): Flow<List<WorkoutSessionDbEntity>>
}