package com.example.cardioproject.anthrodiary.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.cardioproject.anthrodiary.data.local.entity.AnthroMeasurementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AnthroMeasurementDao {

    @Query("SELECT * FROM anthro_measurements ORDER BY measuredAtEpochMillis DESC")
    fun observeAll(): Flow<List<AnthroMeasurementEntity>>

    @Query("SELECT * FROM anthro_measurements ORDER BY measuredAtEpochMillis DESC LIMIT 1")
    fun observeLatest(): Flow<AnthroMeasurementEntity?>

    @Query("SELECT * FROM anthro_measurements WHERE id = :id")
    suspend fun getById(id: Long): AnthroMeasurementEntity?

    @Insert
    suspend fun insert(entity: AnthroMeasurementEntity): Long

    @Query("DELETE FROM anthro_measurements WHERE id = :id")
    suspend fun deleteById(id: Long)
}