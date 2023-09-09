package com.gandiva.android.sample.presentation.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    email: String
) {
    Column() {
        Text(
            modifier = Modifier
                .semantics { testTagsAsResourceId = true;testTag = "welcomeMessageText" },
            text = "Email as explicit argument $email",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier
                .semantics { testTagsAsResourceId = true;testTag = "welcomeMessageText2" },
            text = "Email from saved state handle ${profileViewModel.emailAddress}",
            style = MaterialTheme.typography.titleMedium
        )
    }

}
