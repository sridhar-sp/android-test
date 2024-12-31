package com.gandiva.android.sample.presentation.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val emailAddress = savedStateHandle.get<String>(BundleArgs.KEY_EMAIL)

    var showProgress by mutableStateOf(false)
        private set

    var shouldLogout by mutableStateOf(false)
        private set

    fun logout() {
        viewModelScope.launch {
            showProgress = true
            logoutUseCase.logout(Email(emailAddress))
            showProgress = false
            shouldLogout = true
        }
    }
}