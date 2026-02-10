package com.knthcame.myhealthkmp.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.knthcame.myhealthkmp.ui.dashboard.DashboardScreenRoute
import com.knthcame.myhealthkmp.ui.diary.DiaryScreenRoute
import com.knthcame.myhealthkmp.ui.settings.SettingsScreenRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeDestination

@Serializable
data object Dashboard : HomeDestination

@Serializable
data object Diary : HomeDestination

@Serializable
data object Settings : HomeDestination

@Composable
fun HomeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Dashboard,
        modifier = modifier,
    ) {
        composable<Dashboard> {
            DashboardScreenRoute()
        }
        composable<Diary> {
            DiaryScreenRoute(onNavigationIconClick = { navController.popBackStack() })
        }
        composable<Settings> {
            SettingsScreenRoute(onNavigationIconClick = { navController.popBackStack() })
        }
    }
}