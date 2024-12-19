package com.ilya.mistakereview.screen.event

internal sealed interface Event {
    data object Start : Event
}
