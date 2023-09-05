package com.gandiva.android.sample.profile.ui

import androidx.lifecycle.SavedStateHandle
import com.gandiva.android.sample.profile.constants.BundleArgs
import com.gandiva.android.sample.profile.ui.ProfileViewModel
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test


internal class ProfileViewModelTest {

    @Test
    fun `should return email value from saved state handle when email address is read from viewModel`() {
        val savedStateHandleMock = mockk<SavedStateHandle>()
        every<String?> { savedStateHandleMock[BundleArgs.KEY_EMAIL] } returns "abcd@gmail.com"
        val profileViewModel = ProfileViewModel(savedStateHandleMock)
        assertThat(profileViewModel.emailAddress).isEqualTo("abcd@gmail.com")
    }
}