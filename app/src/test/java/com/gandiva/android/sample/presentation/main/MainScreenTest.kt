package com.gandiva.android.sample.presentation.main

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.gandiva.android.sample.DummyTestActivity
import com.gandiva.android.sample.domain.usecase.LoginUseCase
import com.gandiva.android.sample.presentation.ui.main.MainScreen
import com.gandiva.android.sample.rules.MainCoroutineRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import javax.inject.Inject

/**
 * Example test to show, how a unit test should not be written.
 * More details will add later
 */
@HiltAndroidTest // Provide HiltApplication class details inside robolectric.properties
@RunWith(RobolectricTestRunner::class)
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this) // Sine our component get dependency from hilt, we need this rule

    @get:Rule(order = 1)
    val androidComposeRule = createAndroidComposeRule<DummyTestActivity>()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Before
    fun init() {
        ShadowLog.stream = System.out
//        hiltAndroidRule.inject() // Allow us to provide modified/mock/stub dependencies by using separate module
//        println("***** login use case ${loginUseCase}")
    }

    @Test
    fun shouldSuccessfullyLaunchProfileScreenWithEmailPostLogin() = runTest { // When the logic under test uses coroutine
        with(androidComposeRule) {
            setContent { MainScreen() }

            onNodeWithTag("emailInput").performTextInput("abc@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()

            advanceUntilIdle()

            waitUntil(2500L) {
                onAllNodesWithTag("welcomeMessageText").fetchSemanticsNodes().isNotEmpty()
            }

            onNodeWithTag("welcomeMessageText").assertTextEquals("Email as explicit argument abc@gmail.com")
            onNodeWithTag("welcomeMessageText2")
                .assertTextEquals("Email from saved state handle abc@gmail.com")

            waitForIdle()
        }
    }
}