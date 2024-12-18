package com.ilya.home.screen.event

sealed interface Event {
    data class Search(val query: String) : Event
}
