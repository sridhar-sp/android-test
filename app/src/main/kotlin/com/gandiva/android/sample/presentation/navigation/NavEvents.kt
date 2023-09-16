package com.gandiva.android.sample.presentation.navigation

import com.gandiva.android.sample.domain.model.Email

sealed class NavEvents {
    data class NavigateToLogin(val email: Email) : NavEvents()
}