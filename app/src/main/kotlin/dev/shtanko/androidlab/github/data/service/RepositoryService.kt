package dev.shtanko.androidlab.github.data.service

import com.skydoves.sandwich.ApiResponse
import dev.shtanko.androidlab.github.data.model.NetworkRepository
import retrofit2.http.GET
import retrofit2.http.Query

interface RepositoryService {
    @GET("/users")
    suspend fun fetchRepos(
        @Query("per_page") perPage: Int? = null,
        @Query("page") page: Int? = null,
    ): ApiResponse<List<NetworkRepository>>
}
