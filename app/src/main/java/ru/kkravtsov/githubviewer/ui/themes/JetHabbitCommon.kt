package ru.kkravtsov.githubviewer.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class GithubViewerColors(
    val primaryText: Color,
    val primaryBackground: Color,
    val secondaryText: Color,
    val secondaryBackground: Color,
    val tintColor: Color,
    val controlColor: Color,
    val errorColor: Color
)

data class GithubViewerTypography(
    val heading: TextStyle,
    val body: TextStyle,
    val toolbar: TextStyle,
    val caption: TextStyle
)

data class GithubViewerShape(
    val padding: Dp,
    val cornersStyle: Shape
)

data class GithubViewerImage(
    val mainIcon: Int,
    val mainIconDescription: String
)

object GithubViewerTheme {
    val colors: GithubViewerColors
        @Composable
        get() = LocalGithubViewerColors.current

    val typography: GithubViewerTypography
        @Composable
        get() = LocalGithubViewerTypography.current

    val shapes: GithubViewerShape
        @Composable
        get() = LocalGithubViewerShape.current

    val images: GithubViewerImage
        @Composable
        get() = LocalGithubViewerImage.current
}

enum class GithubViewerStyle {
    Purple, Orange, Blue, Red, Green
}

enum class GithubViewerSize {
    Small, Medium, Big
}

enum class GithubViewerCorners {
    Flat, Rounded
}

val LocalGithubViewerColors = staticCompositionLocalOf<GithubViewerColors> {
    error("No colors provided")
}

val LocalGithubViewerTypography = staticCompositionLocalOf<GithubViewerTypography> {
    error("No font provided")
}

val LocalGithubViewerShape = staticCompositionLocalOf<GithubViewerShape> {
    error("No shapes provided")
}

val LocalGithubViewerImage = staticCompositionLocalOf<GithubViewerImage> {
    error("No images provided")
}