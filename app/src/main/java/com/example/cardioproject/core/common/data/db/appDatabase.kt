package com.example.cardioproject.core.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cardioproject.core.common.data.entity.TagEntity
import com.example.cardioproject.workout.data.dao.WorkoutSessionDao
import com.example.cardioproject.workout.data.entity.WorkoutSessionDbEntity

@Database(
    entities = [TagEntity::class, WorkoutSessionDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class appDatabase: RoomDatabase() {
    abstract fun workoutSessionDao(): WorkoutSessionDao
}