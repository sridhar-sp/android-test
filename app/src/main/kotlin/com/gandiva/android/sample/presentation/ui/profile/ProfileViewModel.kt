package com.gandiva.android.sample.presentation.ui.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)
}