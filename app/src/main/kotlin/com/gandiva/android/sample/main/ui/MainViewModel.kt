package com.gandiva.android.sample.main.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gandiva.android.sample.main.NavEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var navEvents by mutableStateOf<NavEvents?>(null)
        private set

    fun onLoginSuccess(email: String) {
        navEvents = NavEvents.NavigateToLogin(email)
    }
}