package com.gandiva.android.sample.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Profile : Screen("home_screen")
}
