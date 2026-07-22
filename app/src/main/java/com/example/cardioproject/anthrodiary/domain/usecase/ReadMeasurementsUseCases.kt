package com.example.cardioproject.anthrodiary.domain.usecase

import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement
import com.example.cardioproject.anthrodiary.domain.repository.AnthroDiaryRepository
import kotlinx.coroutines.flow.Flow

class ObserveMeasurementsUseCase(
    private val repository: AnthroDiaryRepository
) {
    operator fun invoke(): Flow<List<AnthroMeasurement>> = repository.observeAllMeasurements()
}

/**
 * Пригодится не только этому экрану: например, settings может брать последний
 * известный вес/рост отсюда вместо хранения дублирующегося значения у себя
 * (см. обсуждение дублирования моделей между settings и anthrodiary).
 */
class ObserveLatestMeasurementUseCase(
    private val repository: AnthroDiaryRepository
) {
    operator fun invoke(): Flow<AnthroMeasurement?> = repository.observeLatestMeasurement()
}

class GetMeasurementByIdUseCase(
    private val repository: AnthroDiaryRepository
) {
    suspend operator fun invoke(id: Long): AnthroMeasurement? = repository.getMeasurementById(id)
}

class DeleteMeasurementUseCase(
    private val repository: AnthroDiaryRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteMeasurement(id)
}