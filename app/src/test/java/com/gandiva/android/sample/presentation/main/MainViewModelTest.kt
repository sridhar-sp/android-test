package com.gandiva.android.sample.presentation.main

import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.presentation.navigation.NavEvents
import com.gandiva.android.sample.presentation.ui.main.MainViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class MainViewModelTest {

    @Test
    fun `should set navigation event as login event when onLoginSuccess called`() {
        val mainViewModel = MainViewModel()
        assertThat(mainViewModel.navEvents).isNull()
        mainViewModel.onLoginSuccess(Email("abcd@gmail.com"))
        assertThat(mainViewModel.navEvents).isInstanceOf(NavEvents.NavigateToLogin::class.java)
    }

}