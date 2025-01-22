package dev.shtanko.androidlab.github.data.service

import com.skydoves.sandwich.ApiResponse
import dev.shtanko.androidlab.github.data.model.NetworkUser
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun fetchUsers(
        @Query("per_page") perPage: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ApiResponse<List<NetworkUser>>
}
