package com.example.cardioproject.anthrodiary.domain.model

/**
 * Один сохранённый замер. Все поля обязательны (проверяется в SaveMeasurementUseCase
 * до создания этого объекта — сюда попадают только валидные значения).
 * measuredAtEpochMillis — момент сохранения замера (используется и как ключ сортировки,
 * и для отображения даты). Несколько замеров в один день разрешены осознанно.
 */
data class AnthroMeasurement(
    val id: Long = 0,
    val measuredAtEpochMillis: Long,
    val weightKg: Float,
    val chestCm: Float,
    val waistCm: Float,
    val hipsCm: Float,
    val bicepsCm: Float
)