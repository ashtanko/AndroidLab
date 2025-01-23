package dev.shtanko.androidlab.github.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import java.io.IOException
import java.net.HttpURLConnection

class UserServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var userService: UserService
    private val jsonConfig = Json { ignoreUnknownKeys = true }

    @BeforeEach
    @Throws(IOException::class)
    fun initialize() {
        mockWebServer = MockWebServer()
        userService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(UserService::class.java)
    }

    @AfterEach
    @Throws(IOException::class)
    fun cleanUp() {
        mockWebServer.shutdown()
    }

    @Test
    fun `userService instance should not be null`() {
        assertNotNull(userService)
    }

    @Test
    fun `fetchUsers should return successful response`() = runTest {
        val mockJsonResponse = """
            [
              {
                "id": 1,
                "login": "mojombo",
                "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4"
              }
           ]
        """.trimIndent()

        val successfulResponse = MockResponse().setBody(mockJsonResponse)

        mockWebServer.enqueue(successfulResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val response = userService.fetchUsers()
        if (response is ApiResponse.Success) {
            val users = response.data
            assertNotNull(users)
            assertFalse(users.isEmpty())
            assertEquals("mojombo", users.first().login)
        }
    }

    @Test
    fun `fetchUsers should return failure response`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))
        val response = userService.fetchUsers()
        assertTrue(
            (response as ApiResponse.Failure<*>).toString()
                .contains(HttpURLConnection.HTTP_BAD_REQUEST.toString()),
        )
    }

    @Test
    fun `fetchUsers should return empty response`() = runTest {
        val emptyJsonResponse = """
            [
            ]
        """.trimIndent()

        val emptyResponse = MockResponse().setBody(emptyJsonResponse)

        mockWebServer.enqueue(emptyResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val response = userService.fetchUsers()
        if (response is ApiResponse.Success) {
            val users = response.data
            assertNotNull(users)
            assertTrue(users.isEmpty())
        }
    }
}
