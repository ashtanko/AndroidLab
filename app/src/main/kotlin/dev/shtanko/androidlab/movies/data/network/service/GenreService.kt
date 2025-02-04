package dev.shtanko.androidlab.movies.data.network.service

import com.skydoves.sandwich.ApiResponse
import dev.shtanko.androidlab.movies.data.network.model.genre.NetworkGenres
import retrofit2.http.GET

interface GenreService {
    @GET("genre/movie/list")
    suspend fun fetchMovieGenres(): ApiResponse<NetworkGenres>
}
