package ru.kkravtsov.githubviewer.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.kkravtsov.githubviewer.R
import ru.kkravtsov.githubviewer.domain.SettingsBundle
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerCorners
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerSize
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerStyle
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerTheme
import ru.kkravtsov.githubviewer.ui.themes.blueDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.blueLightPalette
import ru.kkravtsov.githubviewer.ui.themes.greenDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.greenLightPalette
import ru.kkravtsov.githubviewer.ui.themes.orangeDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.orangeLightPalette
import ru.kkravtsov.githubviewer.ui.themes.purpleDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.purpleLightPalette
import ru.kkravtsov.githubviewer.ui.themes.redDarkPalette
import ru.kkravtsov.githubviewer.ui.themes.redLightPalette

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    settings: SettingsBundle,
    onSettingsChanged: (SettingsBundle) -> Unit
) {
    Surface(
        modifier = modifier,
        color = GithubViewerTheme.colors.primaryBackground,
    ) {
        Column(
            Modifier.fillMaxSize()
        ) {
            TopAppBar(
                backgroundColor = GithubViewerTheme.colors.primaryBackground,
                elevation = 8.dp
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = GithubViewerTheme.shapes.padding),
                    text = stringResource(id = R.string.title_settings),
                    color = GithubViewerTheme.colors.primaryText,
                    style = GithubViewerTheme.typography.toolbar
                )
            }

            Row(
                modifier = Modifier.padding(GithubViewerTheme.shapes.padding)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.action_dark_theme_enable),
                    color = GithubViewerTheme.colors.primaryText,
                    style = GithubViewerTheme.typography.body
                )
                Checkbox(
                    checked = settings.isDarkMode, onCheckedChange = {
                        onSettingsChanged.invoke(settings.copy(isDarkMode = !settings.isDarkMode))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = GithubViewerTheme.colors.tintColor,
                        uncheckedColor = GithubViewerTheme.colors.secondaryText
                    )
                )
            }

            Divider(
                modifier = Modifier.padding(start = GithubViewerTheme.shapes.padding),
                thickness = 0.5.dp,
                color = GithubViewerTheme.colors.secondaryText.copy(
                    alpha = 0.3f
                )
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_font_size),
                    currentIndex = when (settings.textSize) {
                        GithubViewerSize.Small -> 0
                        GithubViewerSize.Medium -> 1
                        GithubViewerSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_font_size_small),
                        stringResource(id = R.string.title_font_size_medium),
                        stringResource(id = R.string.title_font_size_big)
                    )
                ),
                onItemSelected = {
                    val settingsNew = settings.copy(
                        textSize = when (it) {
                            0 -> GithubViewerSize.Small
                            1 -> GithubViewerSize.Medium
                            2 -> GithubViewerSize.Big
                            else -> throw NotImplementedError("No valid value for this $it")
                        }
                    )

                    onSettingsChanged.invoke(settingsNew)
                }
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_padding_size),
                    currentIndex = when (settings.paddingSize) {
                        GithubViewerSize.Small -> 0
                        GithubViewerSize.Medium -> 1
                        GithubViewerSize.Big -> 2
                    },
                    values = listOf(
                        stringResource(id = R.string.title_padding_small),
                        stringResource(id = R.string.title_padding_medium),
                        stringResource(id = R.string.title_padding_big)
                    )
                ),
                onItemSelected = {
                    val settingsNew = settings.copy(
                        paddingSize = when (it) {
                            0 -> GithubViewerSize.Small
                            1 -> GithubViewerSize.Medium
                            2 -> GithubViewerSize.Big
                            else -> throw NotImplementedError("No valid value for this $it")
                        }
                    )

                    onSettingsChanged.invoke(settingsNew)
                }
            )

            MenuItem(
                model = MenuItemModel(
                    title = stringResource(id = R.string.title_corners_style),
                    currentIndex = when (settings.cornerStyle) {
                        GithubViewerCorners.Rounded -> 0
                        GithubViewerCorners.Flat -> 1
                    },
                    values = listOf(
                        stringResource(id = R.string.title_corners_style_rounded),
                        stringResource(id = R.string.title_corners_style_flat)
                    )
                ),
                onItemSelected = {
                    val settingsNew = settings.copy(
                        cornerStyle = when (it) {
                            0 -> GithubViewerCorners.Rounded
                            1 -> GithubViewerCorners.Flat
                            else -> throw NotImplementedError("No valid value for this $it")
                        }
                    )

                    onSettingsChanged.invoke(settingsNew)
                }
            )

            Row(
                modifier = Modifier
                    .padding(GithubViewerTheme.shapes.padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(color = if (settings.isDarkMode) purpleDarkPalette.tintColor else purpleLightPalette.tintColor,
                    onClick = {
                        onSettingsChanged.invoke(settings.copy(
                            style = GithubViewerStyle.Purple
                        ))
                    })
                ColorCard(color = if (settings.isDarkMode) orangeDarkPalette.tintColor else orangeLightPalette.tintColor,
                    onClick = {
                        onSettingsChanged.invoke(settings.copy(
                            style = GithubViewerStyle.Orange
                        ))
                    })
                ColorCard(color = if (settings.isDarkMode) blueDarkPalette.tintColor else blueLightPalette.tintColor,
                    onClick = {
                        onSettingsChanged.invoke(settings.copy(
                            style = GithubViewerStyle.Blue
                        ))
                    })
            }

            Row(
                modifier = Modifier
                    .padding(GithubViewerTheme.shapes.padding)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ColorCard(color = if (settings.isDarkMode) redDarkPalette.tintColor else redLightPalette.tintColor,
                    onClick = {
                        onSettingsChanged.invoke(settings.copy(
                            style = GithubViewerStyle.Red
                        ))
                    })
                ColorCard(color = if (settings.isDarkMode) greenDarkPalette.tintColor else greenLightPalette.tintColor,
                    onClick = {
                        onSettingsChanged.invoke(settings.copy(
                            style = GithubViewerStyle.Green
                        ))
                    })
            }
        }
    }
}

@Composable
fun ColorCard(
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(60.dp)
            .clickable {
                onClick.invoke()
            },
        backgroundColor = color,
        elevation = 8.dp,
        shape = GithubViewerTheme.shapes.cornersStyle
    ) { }
}