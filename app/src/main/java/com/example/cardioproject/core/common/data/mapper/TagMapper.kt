package com.example.cardioproject.core.common.data.mapper

import com.example.cardioproject.core.common.data.entity.TagEntity
import com.example.cardioproject.core.common.domain.model.Tag

fun Tag.toEntity(): TagEntity {
    return TagEntity(
        id = this.id,
        name = this.name,
        color = this.color
    )
}

fun TagEntity.toDomain(): Tag {
    return Tag(
        id = this.id,
        name = this.name,
        color = this.color
    )
}