package com.gandiva.android.sample.presentation.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.presentation.navigation.NavEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    var navEvents by mutableStateOf<NavEvents?>(null)
        private set

    fun onLoginSuccess(email: Email) {
        navEvents = NavEvents.NavigateToLogin(email)
    }

    fun onLogout() {
        navEvents = NavEvents.NavigateToHome
    }
}