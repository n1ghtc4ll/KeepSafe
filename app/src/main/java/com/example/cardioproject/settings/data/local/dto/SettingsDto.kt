package com.example.cardioproject.settings.data.local.dto

import kotlinx.serialization.Serializable

/*
@Serializable
data class WorkoutTagDto(
    val id: String,
    val name: String,
    val isEditable: Boolean = true
)
*/

@Serializable
data class HeartRateZoneDto(
    val index: Int,
    val label: String,
    val lowerBpm: Int,
    val upperBpm: Int
)