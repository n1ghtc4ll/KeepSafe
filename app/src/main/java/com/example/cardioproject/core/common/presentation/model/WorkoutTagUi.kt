package com.example.cardioproject.core.common.presentation.model

import com.example.cardioproject.core.common.domain.model.Tag

data class WorkoutTagUi(
    val id: String,
    val name: String,
    val color: String,
    val editable: Boolean = true
)

fun Tag.toUiTag(editable: Boolean = true) = WorkoutTagUi(
    id = this.id,
    name = this.name,
    color = this.color,
    editable = editable
)

fun WorkoutTagUi.toDomain() = Tag(
    id = this.id,
    name = this.name,
    color = this.color
)
