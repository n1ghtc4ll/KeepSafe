package com.example.cardioproject.anthrodiary.domain.usecase

import com.example.cardioproject.anthrodiary.domain.model.AnthroMeasurement
import com.example.cardioproject.anthrodiary.domain.repository.AnthroDiaryRepository

/**
 * Сырой ввод пользователя (строки прямо из текстовых полей UI, ещё не провалидированные).
 */
data class AnthroMeasurementRawInput(
    val weight: String,
    val chest: String,
    val waist: String,
    val hips: String,
    val biceps: String
)

sealed class SaveMeasurementResult {
    data object Success : SaveMeasurementResult()
    data class ValidationError(val message: String) : SaveMeasurementResult()
}

/**
 * Все 5 полей обязательны — по требованию продукта. Если хотя бы одно не заполнено
 * или не парсится как положительное число, сохранение не происходит и возвращается
 * понятная ошибка для показа пользователю (например, в Snackbar).
 */
class SaveMeasurementUseCase(
    private val repository: AnthroDiaryRepository
) {
    suspend operator fun invoke(input: AnthroMeasurementRawInput): SaveMeasurementResult {
        val weight = input.weight.toPositiveFloatOrNull()
            ?: return SaveMeasurementResult.ValidationError("Введите вес")
        val chest = input.chest.toPositiveFloatOrNull()
            ?: return SaveMeasurementResult.ValidationError("Введите обхват груди")
        val waist = input.waist.toPositiveFloatOrNull()
            ?: return SaveMeasurementResult.ValidationError("Введите обхват талии")
        val hips = input.hips.toPositiveFloatOrNull()
            ?: return SaveMeasurementResult.ValidationError("Введите обхват бедер")
        val biceps = input.biceps.toPositiveFloatOrNull()
            ?: return SaveMeasurementResult.ValidationError("Введите обхват бицепса")

        repository.addMeasurement(
            AnthroMeasurement(
                measuredAtEpochMillis = System.currentTimeMillis(),
                weightKg = weight,
                chestCm = chest,
                waistCm = waist,
                hipsCm = hips,
                bicepsCm = biceps
            )
        )
        return SaveMeasurementResult.Success
    }
}

/** Принимает и точку, и запятую как десятичный разделитель (пользователи часто пишут через запятую). */
private fun String.toPositiveFloatOrNull(): Float? =
    trim().replace(',', '.').toFloatOrNull()?.takeIf { it > 0f }