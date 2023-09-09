package com.gandiva.android.sample.presentation.ui.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gandiva.android.sample.domain.usecase.LoginUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCaseImpl
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoginButtonEnabled by mutableStateOf(false)
        private set

    var loginState by mutableStateOf<LoginState>(LoginState.LoginPending)
        private set

    fun updateEmail(emailInput: String) {
        email = emailInput.trim().filterNot(Char::isSurrogate)
        resolveLoginButtonState()
    }

    fun updatePassword(passwordInput: String) {
        password = passwordInput.trim()
        resolveLoginButtonState()
    }

    private fun resolveLoginButtonState() {
        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val isPasswordValid = password.length > 3

        isLoginButtonEnabled = isEmailValid && isPasswordValid
    }


    fun login() {
        viewModelScope.launch {
            loginState = LoginState.InProgress
            loginUseCase.login(email, password)
            loginState = LoginState.LoginSuccess
        }
    }

}
