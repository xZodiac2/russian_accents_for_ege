package com.ilya.training

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilya.data.MistakesRepository
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
class TrainingViewModel @Inject constructor(private val repository: MistakesRepository) : ViewModel() {

    private val allWords = listOf(
        "аэропОрты",
        "бАнты",
        "бОроду",
        "бухгАлтеров",
        "бухгАлтер",
        "вероисповЕдание",
        "водопровОд",
        "газопровОд",
        "граждАнство",
        "дефИс",
        "дешевИзна",
        "диспансЕр",
        "договорЁнность",
        "докумЕнт",
        "досУг",
        "еретИк",
        "жалюзИ",
        "знАчимость",
        "Иксы",
        "каталОг",
        "квартАл",
        "киломЕтр",
        "кОнусов",
        "корЫсть",
        "крАны",
        "кремЕнь",
        "лЕкторов",
        "лЕктор",
        "лОктя",
        "лОкоть",
        "локтЕй",
        "лыжнЯ",
        "мЕстностей",
        "мЕстность",
        "намЕрение",
        "нарОст",
        "нЕдруг",
        "недУг",
        "некролОг",
        "нЕнависть",
        "нефтепровОд",
        "новостЕй",
        "нОвость",
        "нОгтя",
        "нОготь",
        "ногтЕй",
        "Отзыв",
        "отзЫв",
        "Отрочество",
        "партЕр",
        "портфЕль",
        "пОручни",
        "придАное",
        "призЫв",
        "свЁкла",
        "сирОты",
        "сиротА",
        "созЫв",
        "сосредотОчение",
        "срЕдства",
        "срЕдство",
        "стАтуя",
        "столЯр",
        "тамОжня",
        "тОрты",
        "тУфля",
        "цемЕнт",
        "цЕнтнер",
        "цепОчка",
        "шАрфы",
        "шофЁр",
        "экспЕрт",
        "вернА",
        "вЕрный",
        "знАчимый",
        "красИвее",
        "красИвый",
        "красИвейший",
        "кУхонный",
        "ловкА",
        "лОвкий",
        "мозаИчный",
        "оптОвый",
        "прозорлИвый",
        "прозорлИва",
        "слИвовый",
        "бралА",
        "брАть",
        "бралАсь",
        "брАться",
        "взялА",
        "взЯть",
        "взялАсь",
        "взЯться",
        "влилАсь",
        "влИться",
        "ворвалАсь",
        "ворвАться",
        "воспринЯть",
        "воспринялА",
        "воссоздалА",
        "воссоздАть",
        "вручИт",
        "вручИть",
        "гналА",
        "гнАть",
        "гналАсь",
        "гнАться",
        "добралА",
        "добрАть",
        "добралАсь",
        "добрАться",
        "дождалАсь",
        "дождАться",
        "дозвонИтся",
        "дозвонИться",
        "дозИровать",
        "ждалА",
        "ждАть",
        "жилОсь",
        "жИться",
        "закУпорить",
        "занЯть",
        "зАнял",
        "занялА",
        "зАняли",
        "заперлА",
        "заперЕть",
        "запломбировАть",
        "защемИт",
        "защемИть",
        "звалА",
        "звАть",
        "звонИт",
        "звонИть",
        "кАшлянуть",
        "клАла",
        "клАсть",
        "клЕить",
        "крАлась",
        "крАсться",
        "кровоточИть",
        "лгалА",
        "лгАть",
        "лилА",
        "лИть",
        "лилАсь",
        "лИться",
        "навралА",
        "наврАть",
        "наделИт",
        "наделИть",
        "надорвалАсь",
        "надорвАться",
        "назвалАсь",
        "назвАться",
        "накренИтся",
        "накренИться",
        "налилА",
        "налИть",
        "нарвалА",
        "нарвАть",
        "начАть",
        "нАчал",
        "началА",
        "нАчали",
        "обзвонИт",
        "обзвонИть",
        "облегчИть",
        "облилАсь",
        "облИться",
        "обнялАсь",
        "обнЯться",
        "обогналА",
        "обогнАть",
        "ободралА",
        "ободрАть",
        "ободрИть",
        "ободрИт",
        "ободрИться",
        "ободрИтся",
        "обострИть",
        "одолжИть",
        "одолжИт",
        "озлОбить",
        "оклЕить",
        "окружИт",
        "окружИть",
        "опОшлить",
        "освЕдомиться",
        "освЕдомится",
        "отбылА",
        "отбЫть",
        "отдалА",
        "отдАть",
        "откУпорить",
        "отозвалА",
        "отозвАть",
        "отозвалАсь",
        "отозвАться",
        "перезвонИт",
        "перезвонИть",
        "перелилА",
        "перелИть",
        "плодоносИть",
        "пломбировАть",
        "повторИт",
        "повторИть",
        "позвалА",
        "позвАть",
        "позвонИт",
        "позвонИть",
        "полилА",
        "полИть",
        "положИть",
        "положИл",
        "понЯть",
        "понялА",
        "послАла",
        "послАть",
        "прибЫть",
        "прИбыл",
        "прибылА",
        "прИбыли",
        "принЯть",
        "прИнял",
        "принялА",
        "прИняли",
        "рвалА",
        "рвАть",
        "сверлИт",
        "сверлИть",
        "снялА",
        "снЯть",
        "совралА",
        "соврАть",
        "создалА",
        "создАть",
        "сорвалА",
        "сорвАть",
        "сорИт",
        "сорИть",
        "убралА",
        "убрАть",
        "углубИть",
        "укрепИт",
        "укрепИть",
        "чЕрпать",
        "щемИт",
        "щемИть",
        "щЁлкать",
        "довезЁнный",
        "зАгнутый",
        "зАнятый",
        "зАпертый",
        "заселЁнный",
        "кормЯщий",
        "кровоточАщий",
        "нажИвший",
        "налИвший",
        "нанЯвшийся",
        "начАвший",
        "нАчатый",
        "низведЁнный",
        "облегчЁнный",
        "ободрЁнный",
        "обострЁнный",
        "отключЁнный",
        "повторЁнный",
        "поделЁнный",
        "понЯвший",
        "прИнятый",
        "принятА",
        "приручЁнный",
        "прожИвший",
        "снятА",
        "снЯтый",
        "сОгнутый",
        "углублЁнный",
        "закУпорив",
        "начАв",
        "начАвшись",
        "отдАв",
        "поднЯв",
        "понЯв",
        "прибЫв",
        "создАв",
        "вОвремя",
        "дОверху",
        "донЕльзя",
        "дОнизу",
        "дОсуха",
        "зАсветло",
        "зАтемно",
        "красИвее",
        "красИво",
        "надОлго"
    )

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
                val mistakes = repository.getAllMistakes()
                val words = mistakes.flatMap { mistake -> List(mistake.count) { mistake.atWord } }
                WordList(words.shuffled())
            } else {
                WordList(allWords.shuffled())
            }
        }
    }

    private fun onNextPressed(latestWordIndex: Int) {
        clearSolution()

        if (mistakesMode) {
            viewModelScope.launch(Dispatchers.IO) {
                val word = _wordsState.value.words[latestWordIndex]
                repository.getMistakeByWord(word)
                    .onSuccess { mistake ->
                        if (mistake.count - 1 <= 0) {
                            repository.removeMistakeByWord(mistake.atWord)
                            return@onSuccess
                        }
                        repository.saveMistake(mistake.copy(count = mistake.count - 1))
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
                repository.getMistakeByWord(word)
                    .onSuccess { mistake ->
                        val newMistake = mistake.copy(count = mistake.count + 1)
                        repository.saveMistake(newMistake)
                    }
                    .onFailure {
                        when (it) {
                            RepositoryError.MistakeNotFound -> repository.saveMistake(Mistake(word, 1))
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