package ru.kkravtsov.githubviewer.screens.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ru.kkravtsov.githubviewer.screens.search.SearchScreen
import ru.kkravtsov.githubviewer.screens.search.SearchViewModel
import ru.kkravtsov.githubviewer.screens.MainBottomScreen

@ExperimentalFoundationApi
fun NavGraphBuilder.settingsFlow(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation(startDestination = "settings", route = MainBottomScreen.Settings.route) {
        composable("settings") {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController, viewModel = searchViewModel
            )
        }
    }
}