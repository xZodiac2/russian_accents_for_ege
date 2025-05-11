package com.ilya.mistakereview.screen

internal sealed interface Event {
  data object Start : Event
}
