package com.gandiva.android.sample.login.ui

sealed class LoginState {

    object LoginPending : LoginState()
    object InProgress : LoginState()
    object LoginSuccess : LoginState()
}