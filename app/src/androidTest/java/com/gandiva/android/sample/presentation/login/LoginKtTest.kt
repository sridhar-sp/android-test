package com.gandiva.android.sample.presentation.login

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.gandiva.android.sample.domain.usecase.LoginUseCaseImpl
import com.gandiva.android.sample.presentation.ui.login.Login
import com.gandiva.android.sample.presentation.ui.login.LoginViewModel
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
class LoginKtTest {

    @get:Rule
    val composeRule = createComposeRule()

//    @get:Rule
//    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun init() {
    }

    @Test
    fun shouldCallOnSuccessCallbackWithEmailOnSuccessfulLogin() {

        val loginViewModel = LoginViewModel(mockk(relaxed = true))

        var emailFromOnSuccessCallback: String? = null
        val onSuccessMock: (String) -> Unit = { email ->
            emailFromOnSuccessCallback = email
        }


        with(composeRule) {
            setContent { Login(onSuccess = onSuccessMock, viewModel = loginViewModel) }
            onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()
            waitForIdle()
            Truth.assertThat(emailFromOnSuccessCallback).isEqualTo("abcd@gmail.com")
        }
    }

    @Test
    fun shouldEnableButtonOnlyWhenInputsAreValid() {

        val loginUseCase = mockk<LoginUseCaseImpl>(relaxed = true)
        val loginViewModel = LoginViewModel(loginUseCase)

        coEvery { loginUseCase.login(any(), any()) } returns Unit

        with(composeRule) {
            setContent { Login(onSuccess = {}, viewModel = loginViewModel) }
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