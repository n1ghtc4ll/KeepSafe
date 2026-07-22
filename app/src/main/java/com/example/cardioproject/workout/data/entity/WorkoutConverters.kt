package com.example.cardioproject.workout.data.entity

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class WorkoutConverters {
    @TypeConverter
    fun fromHeartRateList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toHeartRateList(value: String): List<Int> {
        if (value.isBlank()) return emptyList()
        return Json.decodeFromString(value)
    }
}