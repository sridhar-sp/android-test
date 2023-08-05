package com.gandiva.android.sample.instrumentation

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val BASIC_SAMPLE_PACKAGE = "com.gandiva.android.sample"
private const val LAUNCH_TIMEOUT = 5000L

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginJourneyTest {

    private lateinit var device: UiDevice

    @get:Rule
    val hiltAndroidRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltAndroidRule.inject()
    }

    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage: String = device.launcherPackageName
        MatcherAssert.assertThat(launcherPackage, CoreMatchers.notNullValue())
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the app
        val context = ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager.getLaunchIntentForPackage(
            BASIC_SAMPLE_PACKAGE
        )?.apply {
            // Clear out any previous instances
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }


    @Test
    fun shouldLaunchProfileScreenWhenLoginIsSuccess() {
        device.enterTextOnFieldWithId("emailInput", "hello@gmail.com")
        device.enterTextOnFieldWithId("passwordInput", "123456")

        device.wait(Until.hasObject(By.res("loginButton").enabled(true)), 1500L)
        assertThat(device.findObject(By.res("loginButton")).isEnabled).isEqualTo(true)

        device.clickFieldWithId("loginButton")

        device.wait(Until.hasObject(By.res("hasObject")), 3000L)

        assertThat(device.textFromFieldWithId("welcomeMessageText"))
            .isEqualTo("Email as explicit argument hello@gmail.com")
        assertThat(device.textFromFieldWithId("welcomeMessageText2"))
            .isEqualTo("Email from saved state handle hello@gmail.com")

        device.waitForIdle()
    }
}