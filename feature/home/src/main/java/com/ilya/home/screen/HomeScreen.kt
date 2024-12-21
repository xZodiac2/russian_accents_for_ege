package com.ilya.home.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilya.home.HomeViewModel
import com.ilya.home.R


@Composable
fun HomeScreen(onTrainingClick: () -> Unit) {
    val viewModel = hiltViewModel<HomeViewModel>()

    val screenState = viewModel.screenState.collectAsState()
    val lazyListState = rememberLazyListState()

    Scaffold(
        containerColor = Color.White,
        topBar = { TopBar(lazyListState) { viewModel.handleEvent(Event.Search(it)) } },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onTrainingClick() },
                containerColor = Color(39, 139, 224, 255),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Практиковаться",
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (screenState.value.words.isEmpty()) {
                Text(text = "Нет результатов", fontSize = 32.sp)
            }
            WordList(lazyListState, screenState.value.words)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(Event.Start)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(lazyListState: LazyListState, onSearch: (String) -> Unit) {
    val contentScrolled by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 50 }
    }
    val dividerColor = animateColorAsState(
        targetValue = if (contentScrolled) Color.Gray else Color(0, 0, 0, 0),
        label = "topBarDividerColorAnimation"
    )
    Column {
        TopAppBar(
            title = { Text("Все слова для ЕГЭ") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            windowInsets = WindowInsets(0, 0)
        )
        SearchBar(
            modifier = Modifier.padding(horizontal = 16.dp),
            onSearch = onSearch
        )
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = dividerColor.value)
    }
}

@Composable
private fun WordList(lazyListState: LazyListState, words: List<List<String>>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(words, key = { it.first() }) { row ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = row[0][0].uppercase(),
                        fontSize = 34.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.LightGray, thickness = 2.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Column {
                        for (word in row) {
                            Text(
                                text = "• $word",
                                fontSize = 22.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier, onSearch: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = { value = it },
        placeholder = {
            Text(
                text = "Поиск слов",
                fontSize = 20.sp
            )
        },
        textStyle = TextStyle(fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(
                    onClick = {
                        value = ""
                        onSearch(value)
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clearSearchBar"
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(value)
                focusManager.clearFocus()
            }
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Black,
            cursorColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}