package com.gandiva.android.sample.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gandiva.android.sample.common.Screen
import com.gandiva.android.sample.login.ui.Login
import com.gandiva.android.sample.main.NavEvents
import com.gandiva.android.sample.profile.ui.Profile

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val navController: NavHostController = rememberNavController()


    LaunchedEffect(key1 = viewModel.navEvents) {
        viewModel.navEvents?.let {
            when (it) {
                is NavEvents.NavigateToLogin ->
                    navController.navigate("${Screen.Profile.route}/${it.email}") {
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

