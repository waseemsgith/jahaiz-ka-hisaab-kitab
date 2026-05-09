package com.waseemsgith.jahaiz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.waseemsgith.jahaiz.ui.home.HomeScreen
import com.waseemsgith.jahaiz.ui.input.InputScreen
import com.waseemsgith.jahaiz.ui.loading.SatireLoadingScreen
import com.waseemsgith.jahaiz.ui.processing.ProcessingScreen
import com.waseemsgith.jahaiz.ui.result.ResultScreen
import com.waseemsgith.jahaiz.ui.splash.SplashScreen

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")

    data object SatireLoading : Screen("satire_loading")

    data object Home : Screen("home")

    data object Input : Screen("input")

    data object Processing : Screen("processing")

    data object Result : Screen("result")
}

@Composable
fun JahaizNavGraph(nav: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = nav,
        startDestination = Screen.Splash.route,
        modifier = modifier,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onDone = {
                    nav.navigate(Screen.SatireLoading.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.SatireLoading.route) {
            SatireLoadingScreen(
                onDone = {
                    nav.navigate(Screen.Home.route) {
                        popUpTo(Screen.SatireLoading.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(onStart = { nav.navigate(Screen.Input.route) })
        }
        composable(Screen.Input.route) {
            InputScreen(
                onSubmitted = { nav.navigate(Screen.Processing.route) },
                onBack = { nav.popBackStack() },
            )
        }
        composable(Screen.Processing.route) {
            ProcessingScreen(
                onDone = { nav.navigate(Screen.Result.route) },
                onFailed = { nav.popBackStack(Screen.Input.route, inclusive = false) },
            )
        }
        composable(Screen.Result.route) {
            ResultScreen(
                onRestart = {
                    nav.popBackStack(Screen.Input.route, inclusive = false)
                },
                onBackHome = {
                    nav.popBackStack(Screen.Home.route, inclusive = false)
                },
            )
        }
    }
}
