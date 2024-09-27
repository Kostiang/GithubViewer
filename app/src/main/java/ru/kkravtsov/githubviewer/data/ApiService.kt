package ru.kkravtsov.githubviewer.data

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kkravtsov.githubviewer.screens.details.RepoContentItem

// Data классы для пользователей и репозиториев
data class UserResponse(val items: List<User>)
data class RepoResponse(val items: List<Repository>)

data class User(
    val login: String,
    val avatar_url: String?,
    val html_url: String,
    val score: Double
)


data class Repository(
    val name: String,
    val forks_count: Int,
    val description: String?,
    val html_url: String,
    val owner: OwnerLogin
): java.io.Serializable

data class OwnerLogin(
    val login: String
)

// Интерфейс ApiService для взаимодействия с GitHub API
interface ApiService {

    // Метод для поиска пользователей
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String
    ): UserResponse

    // Метод для поиска репозиториев
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String
    ): RepoResponse

    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getRepoContents(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path", encoded = true) path: String = ""
    ): List<RepoContentItem>
}
