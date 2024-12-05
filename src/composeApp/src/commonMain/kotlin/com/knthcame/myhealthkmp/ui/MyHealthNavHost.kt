package com.knthcame.myhealthkmp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.knthcame.myhealthkmp.ui.home.HomeScreenRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface TopLevelDestination

@Serializable
data object Home : TopLevelDestination

@Composable
fun MyHealthNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier,
    ) {
        composable<Home> {
            HomeScreenRoute()
        }
    }
}