package ru.kkravtsov.githubviewer.screens.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import ru.kkravtsov.githubviewer.R
import ru.kkravtsov.githubviewer.data.Repository
import ru.kkravtsov.githubviewer.data.User
import ru.kkravtsov.githubviewer.screens.details.ErrorScreen
import ru.kkravtsov.githubviewer.ui.themes.GithubViewerTheme

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SearchViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        var query by remember { mutableStateOf("") }
        Column(
            modifier = modifier.background(color = GithubViewerTheme.colors.primaryBackground),
        ) {
            TopAppBar(
                title = { Text("GitHub Search") },
                actions = {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Введите запрос") },
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.9f),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboard?.hide()
                                viewModel.search(query)
                            }
                        )
                    )
                    IconButton(
                        onClick = { viewModel.search(query) },
                        enabled = query.length >= 3
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                }
            )
            if (uiState.error == null) {
                SearchResultList(uiState, navController)
            }
        }
        if (uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GithubViewerTheme.colors.primaryBackground.copy(alpha = 0.8f)) // Полупрозрачный фон для выделения
                    .zIndex(1f), // Поверх всего контента
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            ErrorScreen(uiState.error.toString()) { viewModel.search(query) }
        }
    }
}

@Composable
fun SearchResultList(uiState: MainUiState, navController: NavController) {
    // Проверяем состояние загрузки и ошибки
    when {
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${uiState.error}", color = Color.Red)
            }
        }

        else -> {
            // Показываем список пользователей и репозиториев
            val context = LocalContext.current
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Отображение списка пользователей
                uiState.users.forEach { user ->
                    item {
                        UserCard(user = user) {
                            // При нажатии открываем ссылку в браузере
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(user.html_url))
                            context.startActivity(intent)
                        }
                    }
                }

                uiState.repositories.forEach { repository ->
                    item {
                        RepositoryCard(repository = repository) {
                            val bundle = Bundle().apply {
                                putSerializable("repo", it)
                            }
                            navController.navigate("repoScreen") {
                                navController.currentBackStackEntry?.arguments?.putSerializable("repository", it)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserCard(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
            .background(color = GithubViewerTheme.colors.primaryBackground),
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = user.avatar_url ?: "")
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_background)
                            error(R.drawable.ic_launcher_background)
                        }).build()
                ),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row {
                Text(modifier = Modifier.weight(1f), text = user.login, fontWeight = FontWeight.Bold)
                Text(text = "Score: ${user.score}", color = Color(0xFFFFA500)) // Оранжевый цвет для score
            }
        }
    }
}

@Composable
fun RepositoryCard(repository: Repository, onClick: (Repository) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick.invoke(repository) })
            .padding(8.dp)
            .background(color = GithubViewerTheme.colors.primaryBackground),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Text(modifier = Modifier.weight(1f), text = repository.name, fontWeight = FontWeight.Bold)
                Text(text = "Forks: ${repository.forks_count}", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = repository.description ?: "No description available",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

