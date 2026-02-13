package com.knthcame.myhealthkmp.ui.diary

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed interface DiaryDestination

@Serializable
data object DiaryList : DiaryDestination

@Serializable
data object AddDiaryEvent : DiaryDestination

inline fun <reified T : Any> NavGraphBuilder.diaryNavigation(
    navHostController: NavHostController,
) {
    navigation<T>(
        startDestination = DiaryList,
    ) {
        composable<DiaryList> {
            DiaryScreenRoute(
                onNavigationIconClick = { navHostController.popBackStack() },
                onFABClicked = { navHostController.navigate(AddDiaryEvent) },
            )
        }
        composable<AddDiaryEvent> {
            AddEventScreenRoute(onNavigationIconClick = { navHostController.popBackStack() })
        }
    }
}