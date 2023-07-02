package com.gandiva.android.sample.profile.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
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

    val profileViewModel: ProfileViewModel = mockk()

    @Before
    fun init() {
        ShadowLog.stream = System.out
    }

    @Test
    fun profile() {
        every {
            profileViewModel.emailAddress
        } returns "abc@gmail.com"

        composeTestRule.setContent {
            Profile(profileViewModel = profileViewModel, email = "abc@gmail.com")
        }

        composeTestRule.onNodeWithTag("welcomeMessageText").assertTextEquals("Email as explicit argument abc@gmail.com")
        composeTestRule.onNodeWithTag("welcomeMessageText2")
            .assertTextEquals("Email from saved state handle abc@gmail.com")
    }
}