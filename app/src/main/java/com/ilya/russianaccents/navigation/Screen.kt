package com.ilya.russianaccents.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {

  @Serializable
  data class Training(val mistakesOnly: Boolean) : Screen

  @Serializable
  data object MistakeReview : Screen

  @Serializable
  data object Home : Screen

}