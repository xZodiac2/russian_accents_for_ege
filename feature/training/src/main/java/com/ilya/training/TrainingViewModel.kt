package com.ilya.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.MistakesRepository
import com.ilya.data.WordsRepository
import com.ilya.data.models.Mistake
import com.ilya.data.repositiory.RepositoryError
import com.ilya.training.screen.event.Event
import com.ilya.training.screen.state.SolutionStatus
import com.ilya.training.screen.state.WordList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TrainingViewModel @Inject constructor(
    private val mistakesRepository: MistakesRepository,
    private val wordsRepository: WordsRepository
) : ViewModel() {

    private val _wordsState = MutableStateFlow(WordList(emptyList()))
    val wordsState = _wordsState.asStateFlow()

    private val _solutionStatus = MutableStateFlow(
        SolutionStatus(
            isSolved = false,
            isCorrect = false,
        )
    )
    val solutionStatus = _solutionStatus.asStateFlow()

    private var mistakesMode = false

    fun handleEvent(event: Event) {
        when (event) {
            is Event.Start -> onStart(event.mistakesOnly)
            is Event.CheckPressed -> checkAccent(event.wordIndex, event.letterIndex)
            is Event.NextPressed -> onNextPressed(event.latestWordIndex)
            Event.LetterSelected -> clearSolution()
        }
    }

    private fun onStart(mistakesOnly: Boolean) {
        mistakesMode = mistakesOnly
        viewModelScope.launch(Dispatchers.IO) {
            _wordsState.value = if (mistakesOnly) {
                val mistakes = mistakesRepository.getAllMistakes()
                val words = mistakes.flatMap { mistake -> List(mistake.count) { mistake.atWord } }
                WordList(words.shuffled())
            } else {
                val allWords = wordsRepository.getAll()
                WordList(allWords.shuffled())
            }
        }
    }

    private fun onNextPressed(latestWordIndex: Int) {
        clearSolution()

        if (mistakesMode) {
            viewModelScope.launch(Dispatchers.IO) {
                val word = _wordsState.value.words[latestWordIndex]
                mistakesRepository.getMistakeByWord(word)
                    .onSuccess { mistake ->
                        if (mistake.count - 1 <= 0) {
                            mistakesRepository.removeMistakeByWord(mistake.atWord)
                            return@onSuccess
                        }
                        mistakesRepository.saveMistake(mistake.copy(count = mistake.count - 1))
                    }
            }
        }
    }

    private fun checkAccent(wordIndex: Int, letterIndex: Int) {
        if (letterIndex == DEFAULT_SELECTED_INDEX) return
        val isCorrect = _wordsState.value.words[wordIndex][letterIndex].isUpperCase()

        _solutionStatus.value = SolutionStatus(
            isSolved = true,
            isCorrect = isCorrect,
        )

        if (!isCorrect) {
            viewModelScope.launch(Dispatchers.IO) {
                val word = _wordsState.value.words[wordIndex]
                mistakesRepository.getMistakeByWord(word)
                    .onSuccess { mistake ->
                        val newMistake = mistake.copy(count = mistake.count + 1)
                        mistakesRepository.saveMistake(newMistake)
                    }
                    .onFailure {
                        when (it) {
                            RepositoryError.MistakeNotFound -> mistakesRepository.saveMistake(Mistake(word, 1))
                        }
                    }
            }
        }
    }

    private fun clearSolution() {
        if (!_solutionStatus.value.isSolved) return
        _solutionStatus.value = SolutionStatus(
            isSolved = false,
            isCorrect = false,
        )
    }

    companion object {
        const val DEFAULT_SELECTED_INDEX = -1
    }

}