package com.gandiva.android.sample.presentation.profile

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.gandiva.android.sample.presentation.ui.profile.Profile
import com.gandiva.android.sample.presentation.ui.profile.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
class ProfileTest {

    @get:Rule
    val composeTestRule = createComposeRule()

//    @get:Rule
//    val hiltAndroidRule = HiltAndroidRule(this)

    @JvmField
//    @BindValue
    val profileViewModel: ProfileViewModel = mockk()

    @Before
    fun init() {
//        hiltAndroidRule.inject() // Not required, since we are mocking and sending args explicitly Profile
    }

    @Test
    fun shouldRenderProfileScreenAsExpected() {
        every {
            profileViewModel.emailAddress
        } returns "abc@gmail.com"

        composeTestRule.setContent {
            Profile(profileViewModel = profileViewModel, email = "abc@gmail.com")
        }

        composeTestRule.onNodeWithTag("welcomeMessageText").assertTextEquals("Email as explicit argument abc@gmail.com")
        composeTestRule.onNodeWithTag("welcomeMessageText2")
            .assertTextEquals("Email from saved state handle abc@gmail.com")

        composeTestRule.waitForIdle()
    }
}