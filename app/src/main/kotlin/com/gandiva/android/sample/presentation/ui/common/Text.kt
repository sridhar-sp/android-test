package com.gandiva.android.sample.presentation.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SingleLineTextInput(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    isEnabled: Boolean = true,
    validationMessage: String? = null,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onKeyboardDone: (() -> Unit)? = null
) {
    DefaultOutlinedTextField(
        modifier = modifier,
        value = value,
        hint = hint,
        isEnabled = isEnabled,
        singleLine = true,
        validationMessage = validationMessage,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        onKeyboardDone = onKeyboardDone,
        visualTransformation = visualTransformation
    )
}

@Composable
fun MultiLineTextInput(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    isEnabled: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = 2,
    validationMessage: String? = null,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onKeyboardDone: (() -> Unit)? = null
) {
    DefaultOutlinedTextField(
        modifier = modifier,
        value = value,
        hint = hint,
        isEnabled = isEnabled,
        singleLine = false,
        minLines = minLines,
        maxLines = maxLines,
        validationMessage = validationMessage,
        onValueChange = onValueChange,
        leadingIcon = leadingIcon,
        keyboardOptions = keyboardOptions,
        onKeyboardDone = onKeyboardDone,
        visualTransformation=visualTransformation
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DefaultOutlinedTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    hint: String,
    isEnabled: Boolean = true,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    validationMessage: String? = null,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onKeyboardDone: (() -> Unit)? = null
) {
    OutlinedTextField(
        enabled = isEnabled,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        textStyle = MaterialTheme.typography.bodyLarge,
        leadingIcon = leadingIcon,
        placeholder = {
            Text(
                text = hint, style = MaterialTheme.typography.bodySmall
            )
        },
        supportingText = {
            validationMessage?.let {
                Text(
                    text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error
                )
            }

        },
        isError = validationMessage != null,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onDone = { onKeyboardDone?.invoke() }),
        visualTransformation = visualTransformation
    )
}