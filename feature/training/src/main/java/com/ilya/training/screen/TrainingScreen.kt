package com.ilya.training.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilya.training.TrainingViewModel
import com.ilya.training.models.BLANK_REPRESENTATION
import com.ilya.training.models.Letter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun TrainingScreen(isMistakesOnly: Boolean, onBackClick: () -> Unit) {
    val viewModel = hiltViewModel<TrainingViewModel>()

    BackHandler { onBackClick() }

    Scaffold(
        topBar = { TopBar(onBackClick) },
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Content(
                wordsStateFlow = viewModel.wordsState,
                solutionStatusStateFlow = viewModel.solutionStatus,
                onBackClick = onBackClick,
                onNextPressed = { viewModel.handleEvent(Event.NextPressed(it)) },
                onLetterSelected = { viewModel.handleEvent(Event.LetterSelected) },
                onCheckPressed = { wordIndex, letterIndex ->
                    viewModel.handleEvent(Event.CheckPressed(wordIndex, letterIndex))
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(Event.Start(isMistakesOnly))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBackClick: () -> Unit) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Практика",
                    fontSize = 28.sp
                )
            },
            navigationIcon = {
                IconButton(onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back"
                    )
                }
            },
            windowInsets = WindowInsets(0, 0),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )
        HorizontalDivider()
    }
}

@Composable
private fun Content(
    wordsStateFlow: StateFlow<WordList>,
    solutionStatusStateFlow: StateFlow<SolutionStatus>,
    onNextPressed: (wordIndex: Int) -> Unit,
    onBackClick: () -> Unit,
    onLetterSelected: () -> Unit,
    onCheckPressed: (wordIndex: Int, letterIndex: Int) -> Unit
) {
    val wordsState = wordsStateFlow.collectAsState()
    val pagerState = rememberPagerState { wordsState.value.words.size }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .verticalScroll(rememberScrollState())
            .animateContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header()
        ProblemsPager(
            pagerState = pagerState,
            wordsState = wordsState,
            solutionStatusStateFlow = solutionStatusStateFlow,
            scope = scope,
            onNextPressed = onNextPressed,
            onBackClick = onBackClick,
            onLetterSelected = onLetterSelected,
            onCheckPressed = onCheckPressed
        )
        Footer(pagerState, wordsState, onBackClick)
    }
}

@Composable
private fun Header() {
    Spacer(modifier = Modifier.height(48.dp))
    Text(
        text = "Тренажер ударений Русского языка",
        fontSize = 26.sp,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ProblemsPager(
    pagerState: PagerState,
    wordsState: State<WordList>,
    solutionStatusStateFlow: StateFlow<SolutionStatus>,
    scope: CoroutineScope,
    onNextPressed: (wordIndex: Int) -> Unit,
    onBackClick: () -> Unit,
    onLetterSelected: () -> Unit,
    onCheckPressed: (wordIndex: Int, letterIndex: Int) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        userScrollEnabled = false
    ) { wordIndex ->
        TrainingContent(
            wordsState = wordsState,
            wordIndex = wordIndex,
            solutionStatusStateFlow = solutionStatusStateFlow,
            onNextWord = {
                onNextPressed(wordIndex)
                scope.launch {
                    if (wordIndex == wordsState.value.words.lastIndex) {
                        onBackClick()
                        return@launch
                    }
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onLetterSelected = { onLetterSelected() },
            onCheckPressed = { letterIndex -> onCheckPressed(wordIndex, letterIndex) }
        )
    }
}

@Composable
private fun TrainingContent(
    wordsState: State<WordList>,
    wordIndex: Int,
    onNextWord: () -> Unit,
    onLetterSelected: () -> Unit,
    onCheckPressed: (Int) -> Unit,
    solutionStatusStateFlow: StateFlow<SolutionStatus>
) {
    val solutionStatus = solutionStatusStateFlow.collectAsState()
    val word = wordsState.value.words[wordIndex]

    Card(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            val lastSelectedLetterIndex = remember { mutableIntStateOf(TrainingViewModel.DEFAULT_SELECTED_INDEX) }
            val isCheckButtonEnalbed = remember(solutionStatus.value) {
                mutableStateOf(!solutionStatus.value.isSolved && lastSelectedLetterIndex.intValue != TrainingViewModel.DEFAULT_SELECTED_INDEX)
            }
            Text(
                text = "Выбери правильное ударение",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            LetterChoice(word, solutionStatus) {
                isCheckButtonEnalbed.value = true
                lastSelectedLetterIndex.intValue = it
                onLetterSelected()
            }
            ResolvingButtonsRow(
                wordIndex = wordIndex,
                lastSelectedLetterIndex = lastSelectedLetterIndex.intValue,
                buttonEnabled = isCheckButtonEnalbed.value,
                wordsState = wordsState,
                solutionStatus = solutionStatus,
                onCheckPressed = onCheckPressed,
                onNextWord = onNextWord
            )
        }
    }
}

@Composable
private fun ResolvingButtonsRow(
    wordIndex: Int,
    lastSelectedLetterIndex: Int,
    buttonEnabled: Boolean,
    wordsState: State<WordList>,
    solutionStatus: State<SolutionStatus>,
    onCheckPressed: (letterIndex: Int) -> Unit,
    onNextWord: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val modifier = if (solutionStatus.value.isCorrect) {
            Modifier.weight(4f)
        } else {
            Modifier.fillMaxWidth()
        }
        Button(
            modifier = modifier,
            enabled = buttonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = calculateCheckButtonColor(lastSelectedLetterIndex, solutionStatus.value),
                disabledContainerColor = calculateCheckButtonColor(lastSelectedLetterIndex, solutionStatus.value),
            ),
            shape = RoundedCornerShape(12.dp),
            onClick = { onCheckPressed(lastSelectedLetterIndex) }
        ) {
            Text(
                text = calculateCheckButtonText(solutionStatus.value),
                fontSize = 16.sp,
                color = calculateCheckButtonTextColor(lastSelectedLetterIndex)
            )
        }
        if (solutionStatus.value.isCorrect) {
            Button(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(39, 139, 224, 255)
                ),
                contentPadding = PaddingValues(0.dp),
                onClick = onNextWord
            ) {
                Icon(
                    imageVector = if (wordIndex == wordsState.value.words.lastIndex) {
                        Icons.AutoMirrored.Filled.ExitToApp
                    } else {
                        Icons.AutoMirrored.Filled.ArrowForward
                    },
                    contentDescription = "continue"
                )
            }
        }
    }
}

@Composable
private fun LetterChoice(word: String, solutionStatus: State<SolutionStatus>, onLetterSelected: (Int) -> Unit) {
    val letters = remember(word) {
        mutableStateOf(word.mapIndexed { index, char -> Letter(index, char.lowercase()) })
    }
    val lettersGrid = remember(letters.value) { letters.value.chunked(6) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (row in lettersGrid.withIndex()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                val fullRow = if (row.index == lettersGrid.lastIndex && lettersGrid.size != 1) {
                    val blanks = (0..5 - row.value.size).map { Letter(-1, BLANK_REPRESENTATION) }
                    row.value.toMutableList().apply { addAll(blanks) }
                } else {
                    row.value
                }
                for (letter in fullRow) {
                    LetterButton(letters, solutionStatus, letter, onLetterSelected)
                }
            }
        }
    }
}

private val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я').map { it.toString() }

@Composable
private fun RowScope.LetterButton(
    letters: MutableState<List<Letter>>,
    solutionStatus: State<SolutionStatus>,
    letter: Letter,
    onLetterSelected: (Int) -> Unit
) {
    val isVowel = remember { letter.representation in vowels }
    val strokeColor = remember(letter.selected, solutionStatus.value) {
        calculateLetterButtonStrokeColor(isVowel, letter.selected, solutionStatus.value)
    }

    val interactionSource = remember { MutableInteractionSource() }
    if (letter.representation == BLANK_REPRESENTATION) {
        Box(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .weight(1f)
                .aspectRatio(1f)
                .background(Color.White)
        )
        return
    }
    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .weight(1f)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(calculateLetterButtonBackground(solutionStatus.value, letter.selected))
            .border(2.dp, strokeColor, RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = isVowel && !solutionStatus.value.isCorrect
            ) {
                val clickedLetter = letter.copy(selected = true)
                val newLetters = letters.value
                    .map { it.copy(selected = false) }
                    .toMutableList()
                newLetters[clickedLetter.index] = clickedLetter
                letters.value = newLetters
                onLetterSelected(letter.index)
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = letter.representation.uppercase(),
            fontSize = 22.sp,
            color = strokeColor
        )
    }
}

@Composable
private fun Footer(pagerState: PagerState, wordsState: State<WordList>, onBackClick: () -> Unit) {
    Text(
        text = "Слово ${pagerState.currentPage + 1} из ${wordsState.value.words.size}",
        fontSize = 20.sp
    )
    Spacer(modifier = Modifier.height(32.dp))
    Button(
        onClick = onBackClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(48, 119, 231, 255)
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(text = "Завершить практику")
    }
    Spacer(modifier = Modifier.height(48.dp))
}

private fun calculateCheckButtonTextColor(letterIndex: Int): Color {
    if (letterIndex == TrainingViewModel.DEFAULT_SELECTED_INDEX) return Color.Gray
    return Color.White
}

private fun calculateCheckButtonText(solutionStatus: SolutionStatus): String {
    if (!solutionStatus.isSolved) return "Проверить"
    return if (solutionStatus.isCorrect) "Правильно" else "Неправильно"
}

private fun calculateCheckButtonColor(letterIndex: Int, solutionStatus: SolutionStatus): Color {
    if (letterIndex == TrainingViewModel.DEFAULT_SELECTED_INDEX) return Color(234, 234, 234, 255)
    if (!solutionStatus.isSolved) return Color(39, 139, 224, 255)

    return if (solutionStatus.isCorrect) Color(127, 204, 127, 255) else Color(248, 102, 102, 255)
}

private fun calculateLetterButtonBackground(solutionStatus: SolutionStatus, isSelected: Boolean): Color {
    if (!isSelected) return Color.White
    if (!solutionStatus.isSolved) return Color(224, 244, 253, 255)
    if (solutionStatus.isCorrect) return Color(203, 255, 191, 255)
    return Color(250, 162, 162, 255)
}

private fun calculateLetterButtonStrokeColor(
    isVowel: Boolean,
    selected: Boolean,
    solutionStatus: SolutionStatus
): Color {
    if (!isVowel) return Color(190, 190, 190, 255)
    if (!selected) return Color.Black
    if (!solutionStatus.isSolved) return Color(74, 133, 194, 255)
    if (solutionStatus.isCorrect) return Color(93, 183, 100, 255)
    return Color(159, 50, 50, 255)
}
