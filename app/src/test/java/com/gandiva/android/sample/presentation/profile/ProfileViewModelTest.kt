package com.gandiva.android.sample.presentation.profile

import androidx.lifecycle.SavedStateHandle
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.usecase.LogoutUseCase
import com.gandiva.android.sample.presentation.ui.profile.BundleArgs
import com.gandiva.android.sample.presentation.ui.profile.ProfileViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Test
    fun `should return email value from saved state handle when email address is read from viewModel`() {
        val savedStateHandleMock = mockk<SavedStateHandle>()
        every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"

        val logoutUseCase = mockk<LogoutUseCase>()

        val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutUseCase)
        assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
    }

    @Test
    fun `should call logout callback when logout button is pressed`() = runTest(testDispatcher) {
        Dispatchers.setMain(testDispatcher)

        val savedStateHandleMock = mockk<SavedStateHandle>()
        every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"

        var isLogoutSuccess = false
        val logoutStub = object : LogoutUseCase {
            override suspend fun logout(email: Email) {
                isLogoutSuccess = true
            }
        }

        val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutStub)
        assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
        assertThat(profileViewModel.shouldLogout).isFalse()

        profileViewModel.logout()
        runCurrent()  // run current co routine to completion

        assertThat(profileViewModel.shouldLogout).isTrue()
        assertThat(isLogoutSuccess).isTrue()
    }
}