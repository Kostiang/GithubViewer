package ru.kkravtsov.githubviewer.screens.tabs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import ru.kkravtsov.githubviewer.data.Repository
import ru.kkravtsov.githubviewer.screens.search.SearchScreen
import ru.kkravtsov.githubviewer.screens.search.SearchViewModel
import ru.kkravtsov.githubviewer.screens.MainBottomScreen
import ru.kkravtsov.githubviewer.screens.details.RepoContentScreen
import ru.kkravtsov.githubviewer.screens.details.RepoContentViewModel

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
fun NavGraphBuilder.searchFlow(
    navController: NavController,
) {
    navigation(startDestination = "search", route = MainBottomScreen.Search.route) {
        composable("search") {
            val searchViewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                navController = navController, viewModel = searchViewModel
            )
        }

        composable(
            "repoScreen",
            arguments = listOf(
                navArgument("repository") {
                    type = NavType.SerializableType(Repository::class.java)
                }
            ),
        ) { backStackEntry ->
            val composeViewModel = hiltViewModel<RepoContentViewModel>()
            val repository = backStackEntry.arguments?.getSerializable("repository") as? Repository
            val owner = repository?.owner?.login ?: ""
            val repo = repository?.name ?: ""
            val initialPath = repository?.html_url ?: ""
            RepoContentScreen(composeViewModel, owner, repo, initialPath) { }
        }
    }
}