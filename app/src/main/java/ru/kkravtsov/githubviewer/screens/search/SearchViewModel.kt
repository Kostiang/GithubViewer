package ru.kkravtsov.githubviewer.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kkravtsov.githubviewer.data.ApiService
import ru.kkravtsov.githubviewer.data.Repository
import ru.kkravtsov.githubviewer.data.User
import javax.inject.Inject

data class MainUiState(
    val users: List<User> = emptyList(),
    val repositories: List<Repository> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun search(query: String) {
        // Не выполнять поиск при коротких запросах
        if (query.length < 3) return

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(loading = true, error = null)

            try {
                // Запуск параллельных запросов через async
                val usersDeferred = async { apiService.searchUsers(query) }
                val reposDeferred = async { apiService.searchRepositories(query) }

                // Ожидание результатов параллельных запросов
                val usersResult = usersDeferred.await()
                val reposResult = reposDeferred.await()

                // Объединение и сортировка результатов в лексикографическом порядке
                val sortedUsers = usersResult.items.sortedBy { it.login }
                val sortedRepos = reposResult.items.sortedBy { it.name }

                _uiState.value = _uiState.value.copy(
                    users = sortedUsers,
                    repositories = sortedRepos,
                    loading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
