package com.gandiva.android.sample.instrumentation

import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector

fun UiDevice.enterTextOnFieldWithId(resourceId: String, text: String) {
    this.findObject(UiSelector().resourceId(resourceId)).text = text
}

fun UiDevice.clickFieldWithId(resourceId: String) {
    this.findObject(By.res(resourceId)).click()
}

fun UiDevice.textFromFieldWithId(resourceId: String): String? {
    return this.findObject(By.res(resourceId)).text
}