package com.ilya.training.screen.state

import androidx.compose.runtime.Immutable

data class SolutionStatus(
    val isSolved: Boolean,
    val isCorrect: Boolean
)

@Immutable
data class WordList(
    val words: List<String>
)
