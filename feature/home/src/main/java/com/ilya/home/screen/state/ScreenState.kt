package com.ilya.home.screen.state

import androidx.compose.runtime.Immutable

@Immutable
data class ScreenState(
    val words: List<List<String>>
)