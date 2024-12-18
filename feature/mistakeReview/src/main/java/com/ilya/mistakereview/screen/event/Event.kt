package com.ilya.mistakereview.screen.event

sealed interface Event {
    data object Start : Event
}
