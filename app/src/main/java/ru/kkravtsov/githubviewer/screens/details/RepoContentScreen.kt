package ru.kkravtsov.githubviewer.screens.details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun RepoContentScreen(
    viewModel: RepoContentViewModel,
    owner: String,
    repo: String,
    initialPath: String = "",
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Запуск загрузки содержимого при первом запуске
    LaunchedEffect(Unit) {
        viewModel.loadRepoContent(owner, repo, initialPath)
    }

    BackHandler(enabled = true) {
        if (!viewModel.handleBack()) {
            onBack() // Закрываем экран, если достигнута корневая папка
        }
    }

    when (uiState) {
        is RepoContentUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RepoContentUiState.Success -> {
            val items = (uiState as RepoContentUiState.Success).items
            RepoContentList(
                items,
                onFolderClick = { path ->
                viewModel.loadRepoContent(owner, repo, path)
            }, onFileClick = { url ->
                // Переход к WebView для просмотра файла
            })
        }

        is RepoContentUiState.Error -> {
            ErrorScreen(
                errorMessage = (uiState as RepoContentUiState.Error).message,
                onRetry = {
                    viewModel.reloadRepo(owner, repo)
                }
            )
        }
    }
}

@Composable
fun RepoContentList(
    items: List<RepoContentItem>,
    onFolderClick: (String) -> Unit,
    onFileClick: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        items.forEach { item ->
            item {
                when (item.type) {
                    "dir" -> {
                        Text(
                            text = "📁 ${item.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onFolderClick(item.path) }
                                .padding(8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    "file" -> {
                        Text(
                            text = "📄 ${item.name}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onFileClick(item.url) }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = onRetry) {
                Text("Reload")
            }
        }
    }
}
