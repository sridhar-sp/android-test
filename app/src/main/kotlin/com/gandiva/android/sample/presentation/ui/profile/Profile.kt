package com.gandiva.android.sample.presentation.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.navigation.compose.hiltViewModel
import com.gandiva.android.sample.R
import com.gandiva.android.sample.presentation.ui.common.PrimaryButton
import com.gandiva.android.sample.presentation.ui.theme.appDimens

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Profile(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    email: String,
    onLogout: () -> Unit
) {
    LaunchedEffect(key1 = profileViewModel.shouldLogout) {
        if (profileViewModel.shouldLogout) onLogout()
    }

    Column {
        Text(
            modifier = Modifier
                .padding(top = MaterialTheme.appDimens.mediumContentPadding)
                .semantics { testTagsAsResourceId = true;testTag = "welcomeMessageText" },
            text = "Email as explicit argument $email",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier
                .padding(vertical = MaterialTheme.appDimens.mediumContentPadding)
                .semantics { testTagsAsResourceId = true;testTag = "welcomeMessageText2" },
            text = "Email from saved state handle ${profileViewModel.emailAddress}",
            style = MaterialTheme.typography.titleMedium
        )

        PrimaryButton(modifier = Modifier
            .semantics { testTagsAsResourceId = true;testTag = "logoutButton" }
            .fillMaxWidth(),
            text = stringResource(id = R.string.logout),
            enabled = true,
            onClick = profileViewModel::logout)

        if (profileViewModel.showProgress)
            CircularProgressIndicator(
                modifier = Modifier
                    .semantics { testTagsAsResourceId = true;testTag = "progressLoader" }
                    .align(Alignment.CenterHorizontally)
                    .padding(MaterialTheme.appDimens.smallContentPadding)
            )
    }
}
