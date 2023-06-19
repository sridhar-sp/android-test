package com.gandiva.android.sample.login.ui

sealed class LoginState {
    object InProgress : LoginState()
    object LoginSuccess : LoginState()
}