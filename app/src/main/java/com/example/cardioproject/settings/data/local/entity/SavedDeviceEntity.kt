package com.example.cardioproject.settings.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_devices")
data class SavedDeviceEntity(
    @PrimaryKey val macAddress: String,
    val name: String,
    val isSelected: Boolean = false
)
