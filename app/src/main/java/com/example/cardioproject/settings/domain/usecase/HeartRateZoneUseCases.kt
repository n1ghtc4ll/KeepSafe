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
        /*require(index in currentZones.indices) { "Некорректный индекс зоны: $index" }
        require(lowerBpm <= upperBpm) { "Нижняя граница не может быть больше верхней" }

        if (index > 0) {
            val previousZone = currentZones[index - 1]
            require(lowerBpm > previousZone.upperBpm) {
                "Нижняя граница ($lowerBpm) пересекает предыдущую зону (${previousZone.upperBpm})"
            }
        }
        if (index < currentZones.lastIndex) {
            val nextZone = currentZones[index + 1]
            require(upperBpm < nextZone.lowerBpm) {
                "Верхняя граница ($upperBpm) пересекает следующую зону (${nextZone.lowerBpm})"
            }
        }

        val updatedZones = currentZones.toMutableList().apply {
            this[index] = this[index].copy(lowerBpm = lowerBpm, upperBpm = upperBpm)
        }
        repository.updateHeartRateZones(updatedZones)*/

        if (index !in currentZones.indices) return

        val oldZone = currentZones[index]
        val updatedZones = currentZones.toMutableList()

        var safeLower = lowerBpm
        var safeUpper = upperBpm

        // Определяем, какую именно границу двигает пользователь в данный момент
        val isLowerChanged = safeLower != oldZone.lowerBpm
        val isUpperChanged = safeUpper != oldZone.upperBpm

        // Защита от инверсии (если свести два пальца вместе, ползунки не должны пересечься)
        if (safeLower >= safeUpper) {
            if (isLowerChanged) safeLower = safeUpper - 1
            else safeUpper = safeLower + 1
        }

        // 1. Если тянем левый край (нижнюю границу)
        if (isLowerChanged && index > 0) {
            val prevZone = updatedZones[index - 1]
            // Не даем "схлопнуть" предыдущую зону (оставляем ей минимум 1 уд/мин)
            safeLower = safeLower.coerceAtLeast(prevZone.lowerBpm + 1)
            // Привязываем верхнюю границу предыдущей зоны к нашей нижней
            updatedZones[index - 1] = prevZone.copy(upperBpm = safeLower - 1)
        }

        // 2. Если тянем правый край (верхнюю границу)
        if (isUpperChanged && index < updatedZones.lastIndex) {
            val nextZone = updatedZones[index + 1]
            // Не даем "схлопнуть" следующую зону
            safeUpper = safeUpper.coerceAtMost(nextZone.upperBpm - 1)
            // Привязываем нижнюю границу следующей зоны к нашей верхней
            updatedZones[index + 1] = nextZone.copy(lowerBpm = safeUpper + 1)
        }

        // Обновляем текущую зону с проверенными границами
        updatedZones[index] = oldZone.copy(lowerBpm = safeLower, upperBpm = safeUpper)

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
    suspend operator fun invoke(currentZones: List<HeartRateZone>, birthDateStr: String) {
        if (birthDateStr.isBlank()) return

        val age = parseAgeFromBirthDate(birthDateStr) ?: return

        val safeAge = if (age in 10..100) age else 30
        val maxHr = 220 - safeAge

        val calculatedZones = listOf(
            HeartRateZone(0, "Зона 1", (maxHr * 0.5).toInt(), (maxHr * 0.6).toInt() - 1),
            HeartRateZone(1, "Зона 2", (maxHr * 0.6).toInt(), (maxHr * 0.7).toInt() - 1),
            HeartRateZone(2, "Зона 3", (maxHr * 0.7).toInt(), (maxHr * 0.8).toInt() - 1),
            HeartRateZone(3, "Зона 4", (maxHr * 0.8).toInt(), maxHr)
        )

        repository.updateHeartRateZones(calculatedZones)
    }

    private fun parseAgeFromBirthDate(birthDateStr: String): Int? {
        return runCatching {
            val year = birthDateStr.split("-", ".", "/")
                .firstOrNull { it.length == 4 }
                ?.toIntOrNull() ?: return null

            val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            currentYear - year
        }.getOrNull()
    }
}