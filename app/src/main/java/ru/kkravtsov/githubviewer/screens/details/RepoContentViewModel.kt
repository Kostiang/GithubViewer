package ru.kkravtsov.githubviewer.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kkravtsov.githubviewer.data.ApiService
import javax.inject.Inject

data class RepoContentItem(
    val name: String,
    val path: String,
    val type: String, // "file" or "dir"
    val url: String
)

sealed class RepoContentUiState {
    object Loading : RepoContentUiState()
    data class Success(val items: List<RepoContentItem>) : RepoContentUiState()
    data class Error(val message: String) : RepoContentUiState()
}

@HiltViewModel
class RepoContentViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<RepoContentUiState>(RepoContentUiState.Loading)
    val uiState: StateFlow<RepoContentUiState> = _uiState.asStateFlow()

    // Стек для хранения истории навигации по путям
    private val navigationStack = mutableListOf<String>()
    private var currentPath = ""

    fun loadRepoContent(owner: String, repo: String, path: String = "") {
        viewModelScope.launch {
            currentPath = path
            _uiState.value = RepoContentUiState.Loading
            try {
                val response = apiService.getRepoContents(owner, repo, path)
                _uiState.value = RepoContentUiState.Success(response)
                if (navigationStack.isEmpty() || navigationStack.last() != path) {
                    navigationStack.add(path) // Добавляем текущий путь в стек
                }
            } catch (e: Exception) {
                _uiState.value = RepoContentUiState.Error(e.message ?: "Error loading content")
            }
        }
    }

    fun reloadRepo(owner: String, repo: String) {
        loadRepoContent(owner, repo, currentPath)
    }

    // Обработка нажатия "Назад" — возврат к предыдущей папке
    fun handleBack(): Boolean {
        return if (navigationStack.size > 1) {
            navigationStack.removeLast() // Удаляем текущий путь
            val previousPath = navigationStack.last() // Получаем предыдущий путь
            loadRepoContent(owner = "", repo = "", path = previousPath) // Переходим к предыдущей папке
            true
        } else {
            false // Возвращаем false, если достигнута корневая папка
        }
    }
}
