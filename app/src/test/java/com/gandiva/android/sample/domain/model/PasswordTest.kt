package com.gandiva.android.sample.domain.model

import com.google.common.truth.Truth
import org.junit.Test

class PasswordTest {

    @Test
    fun shouldReturnIsValidAsFalseWhenPasswordIsNull() {
        Truth.assertThat(Password(null).isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsFalseWhenPasswordIsLessThanFourCharacters() {
        Truth.assertThat(Password("").isValid()).isFalse()
        Truth.assertThat(Password("1").isValid()).isFalse()
        Truth.assertThat(Password("12").isValid()).isFalse()
        Truth.assertThat(Password("123").isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsTrueWhenPasswordGreaterThanOrEqualToFourCharacters() {
        Truth.assertThat(Password("1234").isValid()).isTrue()
        Truth.assertThat(Password("1234568").isValid()).isTrue()
    }
}