package com.ilya.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.WordsRepository
import com.ilya.home.screen.Event
import com.ilya.home.screen.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val wordsRepository: WordsRepository
) : ViewModel() {

    private lateinit var allWords: List<String>

    private val _screenState = MutableStateFlow(ScreenState(emptyList()))
    val screenState = _screenState.asStateFlow()

    fun handleEvent(event: Event) {
        when (event) {
            Event.Start -> onStart()
            is Event.Search -> onSearch(event.query)
        }
    }

    private fun onStart() {
        viewModelScope.launch(Dispatchers.IO) {
            allWords = wordsRepository.getAll()
            _screenState.value = ScreenState(groupWords(allWords))
        }
    }

    private fun onSearch(query: String) {
        val results = allWords.filter { query.lowercase() in it.lowercase() }
        _screenState.value = ScreenState(groupWords(results))
    }

    private fun groupWords(words: List<String>): List<List<String>> {
        return words.groupBy { it[0].lowercase() }.map { it.value }
    }

}