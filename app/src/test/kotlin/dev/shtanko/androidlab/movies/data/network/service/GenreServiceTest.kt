package dev.shtanko.androidlab.movies.data.network.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dev.shtanko.androidlab.movies.data.network.model.genre.NetworkGenre
import dev.shtanko.androidlab.movies.data.network.model.genre.NetworkGenres
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import java.io.IOException
import java.net.HttpURLConnection

class GenreServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private val jsonConfig = Json { ignoreUnknownKeys = true }
    private lateinit var genreService: GenreService

    @BeforeEach
    @Throws(IOException::class)
    fun initialize() {
        mockWebServer = MockWebServer()
        genreService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(GenreService::class.java)
    }

    @AfterEach
    @Throws(IOException::class)
    fun cleanUp() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchMovieGenres should return successful response`() = runTest {
        val mockJsonResponse = """
            {
                "genres": [
                    {
                        "id": 28,
                        "name": "Action"
                    },
                    {
                        "id": 12,
                        "name": "Adventure"
                    }
                ]
            }
        """.trimIndent()

        val successfulResponse = MockResponse().setBody(mockJsonResponse)

        mockWebServer.enqueue(successfulResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val expectedNetworkGenres = NetworkGenres(
            genres = listOf(
                NetworkGenre(28, "Action"),
                NetworkGenre(12, "Adventure"),
            ),
        )

        val response = genreService.fetchMovieGenres()
        require(response is ApiResponse.Success) { "Expected a successful response, but got $response" }
        val genres = response.data
        assertNotNull(genres)
        assertEquals(expectedNetworkGenres, genres)
    }

    @Test
    fun `fetchMovieGenres should return failure response`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))
        val response = genreService.fetchMovieGenres()
        assertTrue(
            (response as ApiResponse.Failure<*>).toString()
                .contains(HttpURLConnection.HTTP_BAD_REQUEST.toString()),
        )
    }

    @Test
    fun `fetchMovieGenres should return empty response`() = runTest {
        val emptyJsonResponse = """
            {
                "genres": []
            }
        """.trimIndent()

        val emptyResponse = MockResponse().setBody(emptyJsonResponse)

        mockWebServer.enqueue(emptyResponse.setResponseCode(HttpURLConnection.HTTP_OK))
        val response = genreService.fetchMovieGenres()
        require(response is ApiResponse.Success) { "Expected a successful response, but got $response" }
        val genres = response.data
        assertNotNull(genres)
        assertTrue(genres.genres.isEmpty())
    }
}
