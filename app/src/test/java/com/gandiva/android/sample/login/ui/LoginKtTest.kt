package com.gandiva.android.sample.login.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.gandiva.android.sample.login.usecase.LoginUseCaseImpl
import com.gandiva.android.sample.rules.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

//@Config(application = HiltTestApplication::class) // added in resources/robolectric.properties
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginKtTest {

    @get:Rule
    val composeRule = createComposeRule()


    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun init() {
        ShadowLog.stream = System.out
    }

    @Test
    fun shouldEnableButtonOnlyWhenInputsAreValid() {
        with(composeRule) {
            val loginUseCase = mockk<LoginUseCaseImpl>()
            val loginViewModel = LoginViewModel(loginUseCase)
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

    @Test
    fun shouldCallOnSuccessCallbackWithEmailOnSuccessfulLogin() = runTest {
        val loginUseCase = mockk<LoginUseCaseImpl>(relaxed = true)

        coEvery { loginUseCase.login(any(), any()) } returns Unit

        val loginViewModel = LoginViewModel(loginUseCase)

        val onSuccessMock: (String) -> Unit = mockk()
        every { onSuccessMock.invoke(any()) } returns Unit

        with(composeRule) {
            setContent { Login(onSuccess = onSuccessMock, viewModel = loginViewModel) }
            onNodeWithTag("emailInput").performTextInput("abcd@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()

            advanceUntilIdle()
            waitForIdle()
            verify { onSuccessMock("abcd@gmail.com") }
        }
    }
}