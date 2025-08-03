package dev.shtanko.lab.app.github.data.service

import com.skydoves.sandwich.ApiResponse
import dev.shtanko.lab.app.github.data.model.NetworkUser
import dev.shtanko.lab.app.github.data.model.NetworkUserFull
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("/users")
    suspend fun fetchUsers(
        @Query("per_page") perPage: Int? = null,
        @Query("limit") limit: Int? = null,
    ): ApiResponse<List<NetworkUser>>

    @GET("/users/{user}")
    suspend fun getFullUser(
        @Path("user") user: String = "google",
    ): ApiResponse<NetworkUserFull>
}
