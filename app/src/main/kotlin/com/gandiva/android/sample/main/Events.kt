package com.gandiva.android.sample.main

sealed class NavEvents {
    data class NavigateToLogin(val email: String) : NavEvents()
}