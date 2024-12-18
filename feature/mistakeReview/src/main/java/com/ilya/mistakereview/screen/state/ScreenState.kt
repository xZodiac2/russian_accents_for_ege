package com.ilya.mistakereview.screen.state

import com.ilya.data.models.Mistake

sealed interface ScreenState {
    data object Loading : ScreenState
    data class MistakesList(val mistakes: List<Mistake>) : ScreenState
}