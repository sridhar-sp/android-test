package com.gandiva.android.sample.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gandiva.android.sample.presentation.navigation.NavEvents
import com.gandiva.android.sample.presentation.navigation.Screen
import com.gandiva.android.sample.presentation.ui.login.Login
import com.gandiva.android.sample.presentation.ui.profile.Profile
import com.gandiva.android.sample.presentation.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController: NavHostController = rememberNavController()


    LaunchedEffect(key1 = viewModel.navEvents) {
        viewModel.navEvents?.let {
            when (it) {
                is NavEvents.NavigateToLogin ->
                    navController.navigate("${Screen.Profile.route}/${it.email.value}") {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
            }
        }
    }


    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            Login(onSuccess = viewModel::onLoginSuccess)
        }
        composable(route = "${Screen.Profile.route}/{email}", arguments = listOf(
            navArgument("email") {
                type = NavType.StringType
                nullable = false
                defaultValue = ""
            }
        )) { backStackEntry ->
            Profile(email = backStackEntry.arguments?.getString("email")!!)
        }

    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}