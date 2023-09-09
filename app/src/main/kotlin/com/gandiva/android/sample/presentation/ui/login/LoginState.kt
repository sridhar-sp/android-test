package com.gandiva.android.sample.presentation.ui.login

sealed class LoginState {

    object LoginPending : LoginState()
    object InProgress : LoginState()
    object LoginSuccess : LoginState()
}