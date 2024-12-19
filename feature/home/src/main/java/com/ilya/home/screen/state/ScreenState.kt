package com.ilya.home.screen.state

import androidx.compose.runtime.Immutable

@Immutable
internal data class ScreenState(
    val words: List<List<String>>
)