package com.example.cardioproject.settings.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cardioproject.settings.data.local.entity.SavedDeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedDevicesDao {
    @Query("SELECT * FROM saved_devices")
    fun getDevices(): Flow<List<SavedDeviceEntity>>

    @Query("SELECT * FROM saved_devices WHERE isSelected = 1 LIMIT 1")
    fun getSelectedDevice(): Flow<SavedDeviceEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: SavedDeviceEntity)

    @Transaction
    suspend fun selectDevice(macAddress: String) {
        clearSelected()
        setSelected(macAddress)
    }
    @Query("UPDATE saved_devices SET isSelected = 1 WHERE macAddress = :macAddress")
    suspend fun setSelected(macAddress: String)
    @Query("UPDATE saved_devices SET isSelected = 0")
    suspend fun clearSelected()

    @Query("DELETE FROM saved_devices WHERE macAddress = :macAddress")
    suspend fun deleteDevice(macAddress: String)
}