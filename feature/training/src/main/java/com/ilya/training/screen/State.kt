package com.ilya.training.screen

import androidx.compose.runtime.Immutable

internal data class SolutionStatus(
    val isSolved: Boolean,
    val isCorrect: Boolean
)

@Immutable
internal data class WordList(
    val words: List<String>
)
