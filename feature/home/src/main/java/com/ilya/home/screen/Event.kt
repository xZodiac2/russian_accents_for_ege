package com.ilya.home.screen

internal sealed interface Event {
  data object Start : Event
  data class Search(val query: String) : Event
}
