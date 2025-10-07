package com.example.findpairgame.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.findpairgame.ui.GameScreen
import com.example.findpairgame.ui.HistoryScreen
import com.example.findpairgame.ui.MainMenuScreen
import com.example.findpairgame.ui.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object MainMenu : Screen("main_menu")
    object Game : Screen("game")
    object History : Screen("history")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val activity = LocalContext.current as? Activity

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.MainMenu.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MainMenu.route) {
            MainMenuScreen(
                onNewGameClick = {
                    navController.navigate(Screen.Game.route)
                },
                onGameHistoryClick = {
                    navController.navigate(Screen.History.route)
                },
                onExitClick = {
                    activity?.finish()
                }
            )
        }

        composable(Screen.Game.route) {
            GameScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
