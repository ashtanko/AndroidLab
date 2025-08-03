package dev.shtanko.lab.app.github.data.service

import com.skydoves.sandwich.ApiResponse
import dev.shtanko.lab.app.github.data.model.NetworkRepository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RepositoryService {
    @GET("/users/{user}/repos")
    suspend fun fetchRepos(
        @Path("user") user: String = "google",
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): ApiResponse<List<NetworkRepository>>
}
