package com.gandiva.android.sample.presentation.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.gandiva.android.sample.R
import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.presentation.ui.common.PrimaryButton
import com.gandiva.android.sample.presentation.ui.common.SingleLineTextInput
import com.gandiva.android.sample.presentation.ui.theme.appDimens


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(onSuccess: (email: Email) -> Unit, viewModel: LoginViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = viewModel.loginState, block = {
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
        EmailInput(modifier = Modifier
            .semantics { testTagsAsResourceId = true;testTag = "emailInput" }
            .testTag("emailInput")
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.appDimens.largeContentPadding),
            value = viewModel.email.value ?: "",
            isEnabled = viewModel.loginState !== LoginState.InProgress,
            onValueChange = viewModel::updateEmail)
        PasswordInput(modifier = Modifier
            .semantics { testTagsAsResourceId = true;testTag = "passwordInput" }
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.appDimens.mediumContentPadding),
            value = viewModel.password.value ?: "",
            isEnabled = viewModel.loginState !== LoginState.InProgress,
            onValueChange = viewModel::updatePassword)
        if (viewModel.loginState === LoginState.LoginPending)
            PrimaryButton(modifier = Modifier
                .semantics { testTagsAsResourceId = true;testTag = "loginButton" }
                .fillMaxWidth(),
                text = stringResource(id = R.string.login),
                enabled = viewModel.isLoginButtonEnabled,
                onClick = viewModel::login)
        if (viewModel.loginState === LoginState.InProgress)
            CircularProgressIndicator(
                modifier = Modifier
                    .semantics { testTagsAsResourceId = true;testTag = "progressLoader" }
                    .align(Alignment.CenterHorizontally)
                    .padding(MaterialTheme.appDimens.smallContentPadding)
            )
    }
}

@Composable
fun EmailInput(modifier: Modifier, value: String, isEnabled: Boolean = true, onValueChange: (String) -> Unit) {
    SingleLineTextInput(
        modifier = modifier,
        value = value,
        hint = stringResource(id = R.string.email),
        isEnabled = isEnabled,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun PasswordInput(modifier: Modifier, value: String, isEnabled: Boolean = true, onValueChange: (String) -> Unit) {
    SingleLineTextInput(
        modifier = modifier,
        value = value,
        hint = stringResource(id = R.string.password),
        isEnabled = isEnabled,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}
