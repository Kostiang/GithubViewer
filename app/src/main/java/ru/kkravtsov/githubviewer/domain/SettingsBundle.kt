package ru.kkravtsov.githubviewer.domain

import ru.kkravtsov.githubviewer.ui.themes.GithubViewerCorners
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerSize
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerStyle

data class SettingsBundle(
    val isDarkMode: Boolean,
    val textSize: GithubViewerSize,
    val paddingSize: GithubViewerSize,
    val cornerStyle: GithubViewerCorners,
    val style: GithubViewerStyle
)