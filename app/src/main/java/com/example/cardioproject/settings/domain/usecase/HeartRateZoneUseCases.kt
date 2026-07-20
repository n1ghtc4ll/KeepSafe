package com.example.cardioproject.settings.domain.usecase

import com.example.cardioproject.settings.domain.model.HeartRateZone
import com.example.cardioproject.settings.domain.repository.SettingsRepository

class UpdateHeartRateZoneUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(
        currentZones: List<HeartRateZone>,
        index: Int,
        lowerBpm: Int,
        upperBpm: Int
    ) {
        require(index in currentZones.indices) { "некорректный индекс зоны: $index" }
        val updatedZones = currentZones.toMutableList().apply {
            this[index] = this[index].copy(lowerBpm = lowerBpm, upperBpm = upperBpm)
        }
        repository.updateHeartRateZones(updatedZones)
    }
}

/**
 * Заглушка под кнопку "Авторасчет зон ЧСС".
 *
 * TODO: формула ещё не согласована (по возрасту / резерву ЧСС по Карвонену — обсуждается).
 * Пока просто пересохраняет текущие зоны без изменений, чтобы UI и слои domain/data
 * уже можно было связать и протестировать. Когда формула определится — заменить тело.
 */
class AutoCalculateHeartRateZonesUseCase(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(currentZones: List<HeartRateZone>) {
        // TODO: реальный расчёт зон вместо no-op
        repository.updateHeartRateZones(currentZones)
    }
}