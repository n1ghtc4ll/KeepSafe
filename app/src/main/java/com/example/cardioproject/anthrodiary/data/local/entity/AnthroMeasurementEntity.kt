package com.example.cardioproject.anthrodiary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anthro_measurements")
data class AnthroMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val measuredAtEpochMillis: Long,
    val weightKg: Float,
    val chestCm: Float,
    val waistCm: Float,
    val hipsCm: Float,
    val bicepsCm: Float
)