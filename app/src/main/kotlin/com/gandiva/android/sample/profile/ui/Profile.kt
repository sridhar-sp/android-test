package com.gandiva.android.sample.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Profile(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    email: String
) {
    Column() {
        Text(
            modifier = Modifier.testTag("welcomeMessageText"),
            text = "Email as explicit argument $email",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier.testTag("welcomeMessageText2"),
            text = "Email from saved state handle ${profileViewModel.emailAddress}",
            style = MaterialTheme.typography.titleMedium
        )
    }

}
