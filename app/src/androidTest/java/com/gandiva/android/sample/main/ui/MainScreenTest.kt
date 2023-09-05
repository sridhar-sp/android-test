package com.gandiva.android.sample.main.ui

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.gandiva.android.sample.DummyTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    /**
     * Need a activity that annotated with @AndroidEntryPoint. and it has to be registered in manifest.
     * Add comment why we used createAndroidComposeRule instead of composeTestRule
     */
    @get:Rule(order = 1)
    val androidComposeRule = createAndroidComposeRule<DummyTestActivity>()


    @Before
    fun init() {
    }

    @Test
    fun shouldSuccessfullyLaunchProfileScreenWithEmailPostLogin() {

        with(androidComposeRule) {
            setContent { MainScreen() }

            onNodeWithTag("emailInput").performTextInput("abc@gmail.com")
            onNodeWithTag("passwordInput").performTextInput("12345")
            onNodeWithTag("loginButton").performClick()

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