package com.ilya.training.screen.event

internal sealed interface Event {
    data class CheckPressed(val wordIndex: Int, val letterIndex: Int) : Event
    data object LetterSelected : Event
    data class NextPressed(val latestWordIndex: Int) : Event
    data class Start(val mistakesOnly: Boolean) : Event
}