package com.gandiva.android.sample.domain.model

import com.google.common.truth.Truth
import org.junit.Test

class EmailTest {

    @Test
    fun shouldReturnIsValidAsFalseWhenEmailIsNull() {
        Truth.assertThat(Email(null).isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsFalseWhenEmailIsInvalid() {
        Truth.assertThat(Email("aa@.com").isValid()).isFalse()
        Truth.assertThat(Email("aacd@aa.com@").isValid()).isFalse()
        Truth.assertThat(Email("").isValid()).isFalse()
        Truth.assertThat(Email("@gmail.com").isValid()).isFalse()
    }

    @Test
    fun shouldReturnIsValidAsTrueWhenEmailIsValid() {
        Truth.assertThat(Email("abcd@domain.com").isValid()).isTrue()
        Truth.assertThat(Email("a@domain.com").isValid()).isTrue()
        Truth.assertThat(Email("a@b.com").isValid()).isTrue()
        Truth.assertThat(Email("a@domain.org").isValid()).isTrue()
        Truth.assertThat(Email("a@domain.in").isValid()).isTrue()
    }
}