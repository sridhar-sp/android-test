package com.gandiva.android.sample.common.ui

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String, onClick: () -> Unit
) {
    Button(modifier = modifier, onClick = onClick, enabled = enabled) {
        Text(
            text = text, style = MaterialTheme.typography.labelLarge
        )
    }
}