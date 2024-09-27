package ru.kkravtsov.githubviewer.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.kkravtsov.githubviewer.domain.SettingsBundle
import ru.kkravtsov.githubviewer.screens.settings.SettingsScreen
import ru.kkravtsov.githubviewer.screens.tabs.searchFlow
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerTheme

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun MainScreen(
    navController: NavController,
    settings: SettingsBundle,
    onSettingsChanged: (SettingsBundle) -> Unit
) {
    val childNavController = rememberNavController()

    // Navigation Items
    val items = listOf(
        MainBottomScreen.Search,
        MainBottomScreen.Settings,
    )

    Column {
        Box(modifier = Modifier.weight(1f)) {
            NavHost(
                navController = childNavController,
                startDestination = MainBottomScreen.Search.route
            ) {
                searchFlow(navController)

                composable(MainBottomScreen.Settings.route) {
                    SettingsScreen(settings = settings, onSettingsChanged = onSettingsChanged)
                }
            }
        }

        Box(
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
        ) {
            BottomNavigation {
                val navBackStackEntry by childNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val previousDestination = remember { mutableStateOf(items.first().route) }

                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy
                        ?.any { it.route == screen.route } == true

                    BottomNavigationItem(
                        modifier = Modifier.background(GithubViewerTheme.colors.primaryBackground),
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    MainBottomScreen.Search -> Icons.Filled.Favorite
                                    MainBottomScreen.Settings -> Icons.Filled.Settings
                                },
                                contentDescription = null,
                                tint = if (isSelected) GithubViewerTheme.colors.tintColor else GithubViewerTheme.colors.controlColor
                            )
                        },
                        label = {
                            Text(
                                stringResource(id = screen.resourceId),
                                color = if (isSelected) GithubViewerTheme.colors.primaryText else GithubViewerTheme.colors.controlColor
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            if (screen.route == previousDestination.value) return@BottomNavigationItem
                            previousDestination.value = screen.route

                            childNavController.navigate(screen.route) {
                                popUpTo(childNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }

                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                }
            }
        }
    }
}