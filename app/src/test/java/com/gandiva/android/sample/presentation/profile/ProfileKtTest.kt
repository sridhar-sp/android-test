package com.gandiva.android.sample.presentation.profile

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.SavedStateHandle
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.usecase.LogoutUseCase
import com.gandiva.android.sample.presentation.ui.profile.BundleArgs
import com.gandiva.android.sample.presentation.ui.profile.Profile
import com.gandiva.android.sample.presentation.ui.profile.ProfileViewModel
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class ProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val profileViewModel: ProfileViewModel = mockk()

    @Before
    fun init() {
        ShadowLog.stream = System.out
    }

    @Test
    fun shouldPrintEmailAddressInProfileScreen() {
        every {
            profileViewModel.emailAddress
        } returns "abc@gmail.com"

        every { profileViewModel.shouldLogout } returns false
        every { profileViewModel.showProgress } returns false

        val onLogoutMock: () -> Unit = {}

        composeTestRule.setContent {
            Profile(profileViewModel = profileViewModel, email = "abc@gmail.com", onLogout = onLogoutMock)
        }

        composeTestRule.onNodeWithTag("welcomeMessageText").assertTextEquals("Email as explicit argument abc@gmail.com")
        composeTestRule.onNodeWithTag("welcomeMessageText2")
            .assertTextEquals("Email from saved state handle abc@gmail.com")
    }

    @Test
    fun shouldCallLogoutCallbackWhenLogoutButtonIsClicked() {
        val savedStateHandleMock = mockk<SavedStateHandle>()
        every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abc@gmail.com"

        var isLogoutMethodInvoked: Boolean = false
        val logoutUseCaseStub = object : LogoutUseCase {
            override suspend fun logout(email: Email) {
                println("Executing logout stub.")
                isLogoutMethodInvoked = true
            }
        }

        val profileViewModel = ProfileViewModel(savedStateHandleMock, logoutUseCaseStub)

        var isLogoutCallbackInvoked: Boolean = false
        val onLogoutMock: () -> Unit = {
            isLogoutCallbackInvoked = true
        }

        with(composeTestRule) {
            setContent {
                Profile(profileViewModel = profileViewModel, email = "abc@gmail.com", onLogout = onLogoutMock)
            }
            onNodeWithTag("welcomeMessageText").assertTextEquals("Email as explicit argument abc@gmail.com")
            onNodeWithTag("welcomeMessageText2").assertTextEquals("Email from saved state handle abc@gmail.com")

            Truth.assertThat(isLogoutCallbackInvoked).isFalse()
            Truth.assertThat(isLogoutMethodInvoked).isFalse()

            onNodeWithTag("logoutButton").performClick()
            waitForIdle()

            Truth.assertThat(isLogoutCallbackInvoked).isTrue()
            Truth.assertThat(isLogoutMethodInvoked).isTrue()

            waitForIdle()
        }

    }
}