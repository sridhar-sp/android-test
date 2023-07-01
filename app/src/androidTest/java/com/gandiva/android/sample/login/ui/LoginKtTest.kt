package com.gandiva.android.sample.login.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun init() {
    }

    @Test
    fun shouldCallOnSuccessCallbackWithEmailOnSuccessfulLogin() {

        var emailFromOnSuccessCallback: String? = null
        val onSuccessMock: (String) -> Unit = { email ->
            emailFromOnSuccessCallback = email
        }

        with(composeRule) {
            setContent { Login(onSuccess = onSuccessMock) }
            onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()
            waitForIdle()
            Truth.assertThat(emailFromOnSuccessCallback).isEqualTo("abcd@gmail.com")
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