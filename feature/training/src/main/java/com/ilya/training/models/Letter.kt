package com.ilya.training.models

internal const val BLANK_REPRESENTATION = "blank"

data class Letter(
  val index: Int,
  val representation: String,
  val selected: Boolean = false,
  val isCorrect: Boolean = false
)
