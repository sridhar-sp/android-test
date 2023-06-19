package com.gandiva.android.sample.profile.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun Profile(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    email: String
) {
    Text(
        text = "Hello $email\nHow are you ${profileViewModel.emailAddress}",
        style = MaterialTheme.typography.titleMedium
    )
}
