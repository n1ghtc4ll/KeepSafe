package com.example.cardioproject.settings.data.local.dto

import kotlinx.serialization.Serializable

@Serializable
data class HeartRateZoneDto(
    val index: Int,
    val label: String,
    val lowerBpm: Int,
    val upperBpm: Int
)