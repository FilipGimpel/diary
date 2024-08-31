package com.gimpel.diary.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gimpel.diary.presentation.map.MapScreen
import com.gimpel.diary.presentation.detail.DetailScreen
import com.gimpel.diary.presentation.list.DiaryEntriesListScreen
import com.gimpel.diary.presentation.navigation.DiaryDestinationsArgs.DETAIL_ID_ARG


@Composable
fun DiaryApp(
    navController: NavHostController = rememberNavController(),
    startDestination: String = DiaryDestinations.LIST_ROUTE,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier.fillMaxSize(),
    ) {
        composable(DiaryDestinations.LIST_ROUTE) {
            DiaryEntriesListScreen(navController = navController)
        }
        composable(DiaryDestinations.MAP_ROUTE) {
            MapScreen(navController = navController)
        }
        composable(
            DiaryDestinations.DETAIL_ROUTE,
            arguments = listOf(navArgument(DETAIL_ID_ARG) { type = NavType.StringType })
        ) {
            DetailScreen(navController = navController)
        }
    }
}


