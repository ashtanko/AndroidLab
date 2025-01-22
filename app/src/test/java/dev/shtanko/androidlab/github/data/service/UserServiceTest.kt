@file:Suppress("ImportOrdering")

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
    private val json = Json { ignoreUnknownKeys = true }

    @BeforeEach
    @Throws(IOException::class)
    fun setUp() {
        mockWebServer = MockWebServer()
        userService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build().create<UserService>(UserService::class.java)
    }

    @AfterEach
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `check user service is not null`() {
        assertNotNull(userService)
    }

    @Test
    fun `get users should return successful response`() = runTest {
        val rawMockResponse = """
            [
              {
                "login": "mojombo",
                "id": 1,
                "node_id": "MDQ6VXNlcjE=",
                "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4",
                "gravatar_id": "",
                "url": "https://api.github.com/users/mojombo",
                "html_url": "https://github.com/mojombo",
                "followers_url": "https://api.github.com/users/mojombo/followers",
                "following_url": "https://api.github.com/users/mojombo/following{/other_user}",
                "gists_url": "https://api.github.com/users/mojombo/gists{/gist_id}",
                "starred_url": "https://api.github.com/users/mojombo/starred{/owner}{/repo}",
                "subscriptions_url": "https://api.github.com/users/mojombo/subscriptions",
                "organizations_url": "https://api.github.com/users/mojombo/orgs",
                "repos_url": "https://api.github.com/users/mojombo/repos",
                "events_url": "https://api.github.com/users/mojombo/events{/privacy}",
                "received_events_url": "https://api.github.com/users/mojombo/received_events",
                "type": "User",
                "user_view_type": "public",
                "site_admin": false
              }
           ]
        """.trimIndent()

        val mockResponse = MockResponse().setBody(rawMockResponse)

        mockWebServer.enqueue(mockResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val result = userService.fetchUsers()
        if (result is ApiResponse.Success) {
            val data = result.data
            assertNotNull(data)
            assertFalse(data.isEmpty())
            assertEquals("mojombo", data.first().login)
        }
    }

    @Test
    fun `get users should return failure response`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))
        val result = userService.fetchUsers()
        assertTrue(
            (result as ApiResponse.Failure<*>).toString()
                .contains(HttpURLConnection.HTTP_BAD_REQUEST.toString()),
        )
    }

    @Test
    fun `get users should return empty response`() = runTest {
        val rawMockResponse = """
            [
            ]
        """.trimIndent()

        val mockResponse = MockResponse().setBody(rawMockResponse)

        mockWebServer.enqueue(mockResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val result = userService.fetchUsers()
        if (result is ApiResponse.Success) {
            val data = result.data
            assertNotNull(data)
            assertTrue(data.isEmpty())
        }
    }
}
