package dev.shtanko.lab.app.github.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dev.shtanko.lab.app.github.data.model.NetworkUserFull
import dev.shtanko.lab.app.utils.toDate
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
import java.util.Date

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
        require(response is ApiResponse.Success) { "Expected a successful response, but got $response" }
        val users = response.data
        assertNotNull(users)
        assertFalse(users.isEmpty())
        assertEquals("mojombo", users.first().login)
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
        require(response is ApiResponse.Success) { "Expected a successful response, but got $response" }
        val users = response.data
        assertNotNull(users)
        assertTrue(users.isEmpty())
    }

    @Test
    fun `getFullUser should return successful response`() = runTest {
        val mockJsonResponse = """
            {
                "id": 10870984,
                "login": "ashtanko",
                "avatar_url": "https://avatars.githubusercontent.com/u/10870984?v=4",
                "name": "Oleksii Shtanko",
                "company": "@EPAM",
                "blog": "https://shtanko.dev",
                "location": "Lisbon, Portugal",
                "hireable": true,
                "bio": "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
                "twitter_username": "shtankopro",
                "public_repos": 29,
                "public_gists": 8,
                "followers": 98,
                "following": 154,
                "created_at": "2015-02-05T19:17:05Z"
            }
    """.trimIndent()

        val successfulResponse = MockResponse().setBody(mockJsonResponse)

        mockWebServer.enqueue(successfulResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val response = userService.getFullUser()
        val expectedUser = NetworkUserFull(
            login = "ashtanko",
            id = 10_870_984,
            avatarUrl = "https://avatars.githubusercontent.com/u/10870984?v=4",
            name = "Oleksii Shtanko",
            company = "@EPAM",
            blog = "https://shtanko.dev",
            location = "Lisbon, Portugal",
            hireable = true,
            bio = "Senior Android Engineer. \r\n\r\nI am passionate about Android, Flutter and Golang",
            twitter = "shtankopro",
            publicReposCount = 29,
            publicGistsCount = 8,
            followers = 98,
            following = 154,
            createdAt = mockDate(),
        )
        require(response is ApiResponse.Success) { "Expected a successful response, but got $response" }
        val user = response.data
        assertNotNull(user)
        assertEquals(expectedUser, user)
    }

    @Test
    fun `getFullUser should return failure response`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))
        val response = userService.getFullUser()
        assertTrue(
            (response as ApiResponse.Failure<*>).toString()
                .contains(HttpURLConnection.HTTP_BAD_REQUEST.toString()),
        )
    }

    private fun mockDate(date: String = "2015-02-05T19:17:05Z"): Date? = date.toDate()
}
