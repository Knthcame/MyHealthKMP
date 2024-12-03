package com.knthcame.myhealthkmp.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import myhealthkmp.composeapp.generated.resources.Res
import myhealthkmp.composeapp.generated.resources.dashboard_title
import myhealthkmp.composeapp.generated.resources.diary_title
import myhealthkmp.composeapp.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreenRoute() {
    val navController = rememberNavController()

    HomeScreen(
        onTabItemClick = { route ->
            navController.navigate(
                route = route,
                navOptions = navOptions {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            )
        },
        navController = navController,
    )
}

@Composable
fun HomeScreen(
    onTabItemClick: (tab: HomeDestination) -> Unit,
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.clickable(
            interactionSource = null,
            indication = null,
            onClick = { focusManager.clearFocus() }),
        bottomBar = {
            HomeBottomBar(
                onTabItemClick = onTabItemClick,
                currentDestination = currentDestination,
            )
        }
    ) { padding ->
        HomeNavHost(
            navController = navController,
            modifier = Modifier.padding(padding),
        )
    }
}

private val bottomBarDestinations: List<HomeDestination> = listOf(
    Dashboard, Diary, Settings,
)

@Preview
@Composable
fun HomeBottomBar(
    onTabItemClick: (destination: HomeDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        modifier = modifier.testTag("homeBottomBar"),
    ) {
        bottomBarDestinations.forEach { destination ->

            val selected = currentDestination?.hierarchy?.any { navDestination ->
                navDestination.hasRoute(destination::class)
            }

            HomeBottomBarItem(
                destination = destination,
                selected = selected ?: false,
                onTabItemClick = onTabItemClick
            )
        }
    }
}

@Composable
private fun RowScope.HomeBottomBarItem(
    destination: HomeDestination,
    selected: Boolean,
    onTabItemClick: (destination: HomeDestination) -> Unit,
) {
    NavigationBarItem(
        modifier = Modifier.testTag(destination.toString()),
        selected = selected,
        onClick = { onTabItemClick(destination) },
        icon = {
            when (destination) {
                Dashboard -> Icon(getHomeIcon(selected), null)
                Diary -> Icon(getDiaryIcon(selected), null)
                Settings -> Icon(getSettingsIcon(selected), null)
            }
        },
        label = {
            val text = when (destination) {
                Dashboard -> stringResource(Res.string.dashboard_title)
                Diary -> stringResource(Res.string.diary_title)
                Settings -> stringResource(Res.string.settings_title)
            }
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            )
        },
    )
}

private fun getHomeIcon(selected: Boolean): ImageVector =
    if (selected)
        Icons.Filled.Home
    else
        Icons.Outlined.Home

private fun getDiaryIcon(selected: Boolean): ImageVector =
    if (selected)
        Icons.AutoMirrored.Filled.List
    else
        Icons.AutoMirrored.Outlined.List

private fun getSettingsIcon(selected: Boolean): ImageVector =
    if (selected)
        Icons.Filled.Settings
    else
        Icons.Outlined.Settings
