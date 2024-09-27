package ru.kkravtsov.githubviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import ru.kkravtsov.githubviewer.data.Repository
import ru.kkravtsov.githubviewer.domain.SettingsBundle
import ru.kkravtsov.githubviewer.screens.MainScreen
import ru.kkravtsov.githubviewer.screens.details.RepoContentScreen
import ru.kkravtsov.githubviewer.screens.details.RepoContentViewModel
import ru.kkravtsov.githubviewer.screens.splash.SplashScreen
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerCorners
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerSize
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerStyle
import ru.kkravtsov.githubviewer.ui.themes.MainTheme
import ru.kkravtsov.githubviewer.ui.themes.baseDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.baseLightPalette

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(
        ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkModeValue = true // isSystemInDarkTheme()

            val currentStyle = remember { mutableStateOf(GithubViewerStyle.Purple) }
            val currentFontSize = remember { mutableStateOf(GithubViewerSize.Medium) }
            val currentPaddingSize = remember { mutableStateOf(GithubViewerSize.Medium) }
            val currentCornersStyle = remember { mutableStateOf(GithubViewerCorners.Rounded) }
            val isDarkMode = remember { mutableStateOf(isDarkModeValue) }

            MainTheme(
                style = currentStyle.value,
                darkTheme = isDarkMode.value,
                textSize = currentFontSize.value,
                corners = currentCornersStyle.value,
                paddingSize = currentPaddingSize.value
            ) {
                val navController = rememberNavController()
                val systemUiController = rememberSystemUiController()

                // Set status bar color
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = if (isDarkMode.value) baseDarkPalette.primaryBackground else baseLightPalette.primaryBackground,
                        darkIcons = !isDarkMode.value
                    )
                }

                Surface {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }

                        composable("main") {
                            val settings = SettingsBundle(
                                isDarkMode = isDarkMode.value,
                                style = currentStyle.value,
                                textSize = currentFontSize.value,
                                cornerStyle = currentCornersStyle.value,
                                paddingSize = currentPaddingSize.value
                            )

                            MainScreen(navController = navController,
                                settings = settings, onSettingsChanged = {
                                    isDarkMode.value = it.isDarkMode
                                    currentStyle.value = it.style
                                    currentFontSize.value = it.textSize
                                    currentCornersStyle.value = it.cornerStyle
                                    currentPaddingSize.value = it.paddingSize
                                }
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
            }
        }
    }
}