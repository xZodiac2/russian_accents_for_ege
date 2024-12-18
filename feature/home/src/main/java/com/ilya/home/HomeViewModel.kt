package com.ilya.home

import androidx.lifecycle.ViewModel
import com.ilya.home.screen.event.Event
import com.ilya.home.screen.state.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

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
    ).sortedBy { it.first().lowercase() }

    private fun sortWords(words: List<String>): List<List<String>> {
        return words.groupBy { it[0].lowercase() }.map { it.value }
    }

    private val _screenState = MutableStateFlow(ScreenState(sortWords(allWords)))
    val screenState = _screenState.asStateFlow()

    fun handleEvent(event: Event) {
        when (event) {
            is Event.Search -> onSearch(event.query)
        }
    }

    private fun onSearch(query: String) {
        val results = allWords.filter { query.lowercase() in it.lowercase() }
        _screenState.value = ScreenState(sortWords(results))
    }

}