package ru.kkravtsov.githubviewer.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kkravtsov.githubviewer.R

@Composable
fun MainTheme(
    style: GithubViewerStyle = GithubViewerStyle.Purple,
    textSize: GithubViewerSize = GithubViewerSize.Medium,
    paddingSize: GithubViewerSize = GithubViewerSize.Medium,
    corners: GithubViewerCorners = GithubViewerCorners.Rounded,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                GithubViewerStyle.Purple -> purpleDarkPalette
                GithubViewerStyle.Blue -> blueDarkPalette
                GithubViewerStyle.Orange -> orangeDarkPalette
                GithubViewerStyle.Red -> redDarkPalette
                GithubViewerStyle.Green -> greenDarkPalette
            }
        }
        false -> {
            when (style) {
                GithubViewerStyle.Purple -> purpleLightPalette
                GithubViewerStyle.Blue -> blueLightPalette
                GithubViewerStyle.Orange -> orangeLightPalette
                GithubViewerStyle.Red -> redLightPalette
                GithubViewerStyle.Green -> greenLightPalette
            }
        }
    }

    val typography = GithubViewerTypography(
        heading = TextStyle(
            fontSize = when (textSize) {
                GithubViewerSize.Small -> 24.sp
                GithubViewerSize.Medium -> 28.sp
                GithubViewerSize.Big -> 32.sp
            },
            fontWeight = FontWeight.Bold
        ),
        body = TextStyle(
            fontSize = when (textSize) {
                GithubViewerSize.Small -> 14.sp
                GithubViewerSize.Medium -> 16.sp
                GithubViewerSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Normal
        ),
        toolbar = TextStyle(
            fontSize = when (textSize) {
                GithubViewerSize.Small -> 14.sp
                GithubViewerSize.Medium -> 16.sp
                GithubViewerSize.Big -> 18.sp
            },
            fontWeight = FontWeight.Medium
        ),
        caption = TextStyle(
            fontSize = when (textSize) {
                GithubViewerSize.Small -> 10.sp
                GithubViewerSize.Medium -> 12.sp
                GithubViewerSize.Big -> 14.sp
            }
        )
    )

    val shapes = GithubViewerShape(
        padding = when (paddingSize) {
            GithubViewerSize.Small -> 12.dp
            GithubViewerSize.Medium -> 16.dp
            GithubViewerSize.Big -> 20.dp
        },
        cornersStyle = when (corners) {
            GithubViewerCorners.Flat -> RoundedCornerShape(0.dp)
            GithubViewerCorners.Rounded -> RoundedCornerShape(8.dp)
        }
    )

    val images = GithubViewerImage(
        mainIcon = if (darkTheme) R.drawable.ic_baseline_mood_24 else R.drawable.ic_baseline_mood_bad_24,
        mainIconDescription = if (darkTheme) "Good Mood" else "Bad Mood"
    )

    CompositionLocalProvider(
        LocalGithubViewerColors provides colors,
        LocalGithubViewerTypography provides typography,
        LocalGithubViewerShape provides shapes,
        LocalGithubViewerImage provides images,
        content = content
    )
}