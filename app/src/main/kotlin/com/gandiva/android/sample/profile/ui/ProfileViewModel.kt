package com.gandiva.android.sample.profile.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.gandiva.android.sample.profile.constants.BundleArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)
}