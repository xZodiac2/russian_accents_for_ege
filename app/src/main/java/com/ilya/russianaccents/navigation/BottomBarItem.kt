package com.ilya.russianaccents.navigation

import androidx.annotation.DrawableRes
import com.ilya.russianaccents.R

sealed class BottomBarItem(
  val label: String,
  @DrawableRes
  val iconId: Int,
  val screen: Screen
) {

  data object Home : BottomBarItem(
    label = "Список слов",
    iconId = R.drawable.list,
    screen = Screen.Home
  )

  data object MistakeReview : BottomBarItem(
    label = "Ошибки",
    iconId = R.drawable.cross,
    screen = Screen.MistakeReview
  )

}