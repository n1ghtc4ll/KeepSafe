package com.example.cardioproject.anthrodiary.data.mapper

import com.example.cardioproject.anthrodiary.data.local.entity.AnthroMeasurementEntity
import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement

fun AnthroMeasurementEntity.toDomain() = AnthroMeasurement(
    id = id,
    measuredAtEpochMillis = measuredAtEpochMillis,
    weightKg = weightKg,
    chestCm = chestCm,
    waistCm = waistCm,
    hipsCm = hipsCm,
    bicepsCm = bicepsCm
)

fun AnthroMeasurement.toEntity() = AnthroMeasurementEntity(
    id = id,
    measuredAtEpochMillis = measuredAtEpochMillis,
    weightKg = weightKg,
    chestCm = chestCm,
    waistCm = waistCm,
    hipsCm = hipsCm,
    bicepsCm = bicepsCm
)