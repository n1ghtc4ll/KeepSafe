package com.example.cardioproject.anthrodiary.data.repository

import com.example.cardioproject.anthrodiary.data.local.dao.AnthroMeasurementDao
import com.example.cardioproject.anthrodiary.data.mapper.toDomain
import com.example.cardioproject.anthrodiary.data.mapper.toEntity
import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement
import com.example.cardioproject.anthrodiary.domain.repository.AnthroDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnthroDiaryRepositoryImpl(
    private val dao: AnthroMeasurementDao
) : AnthroDiaryRepository {

    override fun observeAllMeasurements(): Flow<List<AnthroMeasurement>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override fun observeLatestMeasurement(): Flow<AnthroMeasurement?> =
        dao.observeLatest().map { it?.toDomain() }

    override suspend fun getMeasurementById(id: Long): AnthroMeasurement? =
        dao.getById(id)?.toDomain()

    override suspend fun addMeasurement(measurement: AnthroMeasurement): Long =
        dao.insert(measurement.toEntity())

    override suspend fun deleteMeasurement(id: Long) = dao.deleteById(id)
}