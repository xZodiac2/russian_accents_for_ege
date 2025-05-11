package com.ilya.training.screen

internal sealed interface Event {
  data class CheckPressed(val wordIndex: Int, val letterIndex: Int) : Event
  data object LetterSelected : Event
  data class NextPressed(val latestWordIndex: Int) : Event
  data class Start(val isMistakesOnly: Boolean) : Event
}