package com.example.cardioproject.anthrodiary.domain.repository

import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement
import kotlinx.coroutines.flow.Flow

interface AnthroDiaryRepository {

    /** Все замеры, от новых к старым. */
    fun observeAllMeasurements(): Flow<List<AnthroMeasurement>>

    /** Самый последний сохранённый замер (или null, если история пуста). */
    fun observeLatestMeasurement(): Flow<AnthroMeasurement?>

    suspend fun getMeasurementById(id: Long): AnthroMeasurement?

    /** Возвращает id только что вставленной записи. */
    suspend fun addMeasurement(measurement: AnthroMeasurement): Long

    suspend fun deleteMeasurement(id: Long)
}