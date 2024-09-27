package ru.kkravtsov.githubviewer.screens

import androidx.annotation.StringRes
import ru.kkravtsov.githubviewer.R

sealed class MainBottomScreen(val route: String, @StringRes val resourceId: Int) {
    object Search : MainBottomScreen("searchFlow", R.string.title_daily)
    object Settings : MainBottomScreen("settingsFlow", R.string.title_settings)
}
