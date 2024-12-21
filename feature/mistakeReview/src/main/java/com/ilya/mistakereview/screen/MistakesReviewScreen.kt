package com.ilya.mistakereview.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilya.data.models.Mistake
import com.ilya.mistakereview.MistakesReviewViewModel
import com.ilya.mistakereview.R

@Composable
fun MistakesReviewScreen(onTrainingClick: () -> Unit) {
    val viewModel = hiltViewModel<MistakesReviewViewModel>()

    val screenState = viewModel.screenState.collectAsState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = Color.White,
        topBar = { TopBar(lazyListState, screenState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val state = screenState.value as? ScreenState.MistakesList
                    if (state?.mistakes?.isNotEmpty() == true) {
                        onTrainingClick()
                    }
                },
                containerColor = Color(39, 139, 224, 255),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Отработать ошибки",
                        color = Color.White
                    )
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.dumbbell),
                        contentDescription = "training",
                        tint = Color.White
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(bottom = 0)
    ) { padding ->
        when (val state = screenState.value) {
            ScreenState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.DarkGray)
                }
            }

            is ScreenState.MistakesList -> Mistakes(padding, lazyListState, state.mistakes)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(Event.Start)
    }
}

@Composable
private fun Mistakes(padding: PaddingValues, lazyListState: LazyListState, mistakes: List<Mistake>) {
    if (mistakes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "У вас пока нет ошибок",
                fontSize = 28.sp
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mistakes) { mistake ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = mistake.atWord,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "Ошибки: ${mistake.count}",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(lazyListState: LazyListState, screenState: State<ScreenState>) {
    val stateValue = screenState.value
    val topBarScrolled by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 10 }
    }
    val dividerColor = animateColorAsState(
        targetValue = if (topBarScrolled) Color.Gray else Color.White
    )
    Column {
        TopAppBar(
            title = { Text(text = "Ваши ошибки") },
            windowInsets = WindowInsets(0, 0),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        HorizontalDivider(
            color = if (stateValue is ScreenState.MistakesList && stateValue.mistakes.isEmpty()) {
                Color.Gray
            } else {
                dividerColor.value
            }
        )
    }
}
