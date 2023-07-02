package com.gandiva.android.sample.login.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class LoginKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun init() {
        ShadowLog.stream = System.out
    }

    @Test
    fun shouldCallOnSuccessCallbackWithEmailOnSuccessfulLogin() {

        val onSuccessMock: (String) -> Unit = mockk()
        every { onSuccessMock.invoke(any()) } returns Unit

        with(composeRule) {
            setContent { Login(onSuccess = onSuccessMock) }
            onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()
            waitForIdle()
            verify { onSuccessMock("abcd@gmail.com") }
        }
    }

    @Test
    fun shouldEnableButtonOnlyWhenInputsAreValid() {
        with(composeRule) {
            setContent { Login(onSuccess = {}) }
            onNodeWithTag("loginButton").assertIsNotEnabled()

            onNodeWithTag("emailInput").performTextInput("abcd")
            onNodeWithTag("loginButton").assertIsNotEnabled()

            onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
            onNodeWithTag("loginButton").assertIsNotEnabled()

            onNodeWithTag("passwordInput").performTextInput("12")
            onNodeWithTag("loginButton").assertIsNotEnabled()

            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").assertIsEnabled()
        }
    }
}