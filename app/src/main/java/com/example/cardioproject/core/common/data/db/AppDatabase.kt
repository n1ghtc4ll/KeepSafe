package com.example.cardioproject.core.common.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cardioproject.anthrodiary.data.local.dao.AnthroMeasurementDao
import com.example.cardioproject.anthrodiary.data.local.entity.AnthroMeasurementEntity
import com.example.cardioproject.core.common.data.dao.TabataProfileDao
import com.example.cardioproject.core.common.data.dao.TagDao
import com.example.cardioproject.core.common.data.entity.TabataProfileEntity
import com.example.cardioproject.core.common.data.entity.TagEntity
import com.example.cardioproject.workout.data.dao.WorkoutSessionDao
import com.example.cardioproject.workout.data.entity.WorkoutSessionDbEntity

@Database(
    entities = [TagEntity::class,
        WorkoutSessionDbEntity::class,
        TabataProfileEntity::class,
        AnthroMeasurementEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun tagDao(): TagDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun tabataProfileDao(): TabataProfileDao
    abstract fun anthroMeasurementDao(): AnthroMeasurementDao
}