package com.gandiva.android.sample.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.gandiva.android.sample.R
import com.gandiva.android.sample.common.ui.PrimaryButton
import com.gandiva.android.sample.common.ui.SingleLineTextInput
import com.gandiva.android.sample.theme.appDimens
import timber.log.Timber


@Composable
fun Login(onSuccess: (email: String) -> Unit, viewModel: LoginViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = viewModel.loginState, block = {
        Timber.d("LaunchedEffect  viewModel.loginState ${viewModel.loginState}")
        if (viewModel.loginState == LoginState.LoginSuccess) onSuccess(viewModel.email)
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.appDimens.mediumContentPadding),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = MaterialTheme.appDimens.largeContentPadding),
            text = stringResource(id = R.string.login),
            style = MaterialTheme.typography.headlineMedium
        )
        EmailInput(
            modifier = Modifier
                .testTag("emailInput")
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.appDimens.largeContentPadding),
            value = viewModel.email,
            onValueChange = viewModel::updateEmail
        )
        PasswordInput(
            modifier = Modifier
                .testTag("passwordInput")
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.appDimens.mediumContentPadding),
            value = viewModel.password,
            onValueChange = viewModel::updatePassword
        )
        PrimaryButton(
            modifier = Modifier
                .testTag("loginButton")
                .fillMaxWidth(),
            text = stringResource(id = R.string.login),
            enabled = viewModel.isLoginButtonEnabled,
            onClick = viewModel::login
        )
    }
}

@Composable
fun EmailInput(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
    SingleLineTextInput(
        modifier = modifier,
        value = value,
        hint = stringResource(id = R.string.email),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordInput(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
    SingleLineTextInput(
        modifier = modifier,
        value = value,
        hint = stringResource(id = R.string.password),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}
