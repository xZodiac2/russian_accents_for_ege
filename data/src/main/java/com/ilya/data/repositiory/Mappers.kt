package com.ilya.data.repositiory

import com.ilya.data.database.entity.MistakeEntity
import com.ilya.data.models.Mistake

internal fun MistakeEntity.toMistake(): Mistake = Mistake(atWord, count)

internal fun Mistake.toEntity(): MistakeEntity = MistakeEntity(atWord, count)