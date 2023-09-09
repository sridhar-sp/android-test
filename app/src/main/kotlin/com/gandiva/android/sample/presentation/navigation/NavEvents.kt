package com.gandiva.android.sample.presentation.navigation

sealed class NavEvents {
    data class NavigateToLogin(val email: String) : NavEvents()
}