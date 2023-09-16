package com.gandiva.android.sample.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.model.Password
import com.gandiva.android.sample.domain.usecase.LoginUseCaseImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCaseImpl
) : ViewModel() {

    var email by mutableStateOf(Email(null))
        private set

    var password by mutableStateOf(Password(null))
        private set

    var isLoginButtonEnabled by mutableStateOf(false)
        private set

    var loginState by mutableStateOf<LoginState>(LoginState.LoginPending)
        private set

    fun updateEmail(emailInput: String) {
        email = Email(emailInput.trim().filterNot(Char::isSurrogate))
        resolveLoginButtonState()
    }

    fun updatePassword(passwordInput: String) {
        password = Password(passwordInput.trim())
        resolveLoginButtonState()
    }

    private fun resolveLoginButtonState() {
        val isEmailValid = email.isValid()
        val isPasswordValid = password.isValid()

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
