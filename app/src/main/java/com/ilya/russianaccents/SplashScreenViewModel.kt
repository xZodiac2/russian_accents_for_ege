package com.ilya.russianaccents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel() {

    var isLoading = true
        private set

    init {
        viewModelScope.launch {
            delay(2000)
            isLoading = false
        }
    }

}