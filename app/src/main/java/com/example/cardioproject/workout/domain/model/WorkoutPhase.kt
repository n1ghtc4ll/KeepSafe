package com.example.cardioproject.workout.domain.model

enum class WorkoutPhase(val phaseName: String) {
    WARMUP("Пауза перед тренировкой"),
    WORKOUT("Тренировка"),
    RELAX("Отдых"),
    BREAK("Перезарядка"),
    COOLDOWN("Заминка"),
    FINISHED("Завершение"),
}