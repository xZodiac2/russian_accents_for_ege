package com.ilya.mistakereview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.MistakesRepository
import com.ilya.mistakereview.screen.Event
import com.ilya.mistakereview.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MistakesReviewViewModel @Inject constructor(
    private val repository: MistakesRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow<ScreenState>(ScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    fun handleEvent(event: Event) {
        when (event) {
            Event.Start -> onStart()
        }
    }

    private fun onStart() {
        viewModelScope.launch {
            val mistakes = repository.getAllMistakes()
            _screenState.value = ScreenState.MistakesList(mistakes)
        }
    }

}