package dev.shtanko.androidlab.github.data.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dev.shtanko.androidlab.github.data.model.NetworkRepository
import dev.shtanko.androidlab.github.data.model.NetworkUser
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

class RepositoryServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private val jsonConfig = Json { ignoreUnknownKeys = true }
    private lateinit var repositoryService: RepositoryService

    @BeforeEach
    @Throws(IOException::class)
    fun initialize() {
        mockWebServer = MockWebServer()
        repositoryService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/").toString())
            .addConverterFactory(jsonConfig.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(RepositoryService::class.java)
    }

    @AfterEach
    @Throws(IOException::class)
    fun cleanUp() {
        mockWebServer.shutdown()
    }

    @Test
    fun `repositoryService instance should not be null`() {
        assertNotNull(repositoryService)
    }

    @Suppress("LongMethod")
    @Test
    fun `fetchRepos should return successful response`() = runTest {
        val mockJsonResponse = """
            [
               {
                "id": 1,
                "node_id" = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
                "name": "Sample Repo",
                "full_name": "sample/sample-repo",
                "private": false,
                "description": "A sample repository",
                "fork": false,
                "size": 1024,
                "stargazers_count": 150,
                "watchers_count": 75,
                "forks_count": 10,
                "language": "Kotlin",
                "has_issues": true,
                "has_projects": true,
                "archived": false,
                "disabled": false,
                "open_issues_count": 5,
                "is_template": false,
                "owner": {
                    "id": 1,
                    "login": "sample",
                    "avatar_url": "https://avatars.githubusercontent.com/u/1?v=4"
                }
            }
           ]
        """.trimIndent()

        val successfulResponse = MockResponse().setBody(mockJsonResponse)

        mockWebServer.enqueue(successfulResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val expectedList = listOf(
            NetworkRepository(
                id = 1,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=",
                name = "Sample Repo",
                fullName = "sample/sample-repo",
                isPrivate = false,
                description = "A sample repository",
                isFork = false,
                size = 1024,
                stars = 150,
                watchers = 75,
                forks = 10,
                language = "Kotlin",
                hasIssues = true,
                hasProjects = true,
                archived = false,
                disabled = false,
                openIssues = 5,
                isTemplate = false,
                owner = NetworkUser(
                    id = 1,
                    login = "sample",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                ),
            ),
        )

        val response = repositoryService.fetchRepos()

        if (response is ApiResponse.Success) {
            val repos = response.data
            assertNotNull(repos)
            assertFalse(repos.isEmpty())
            assertEquals(expectedList, repos)
        }
    }

    @Test
    fun `fetchRepos should return failure response`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST))
        val response = repositoryService.fetchRepos()
        assertTrue(
            (response as ApiResponse.Failure<*>).toString()
                .contains(HttpURLConnection.HTTP_BAD_REQUEST.toString()),
        )
    }

    @Test
    fun `fetchRepos should return empty response`() = runTest {
        val emptyJsonResponse = """
            [
            ]
        """.trimIndent()

        val emptyResponse = MockResponse().setBody(emptyJsonResponse)

        mockWebServer.enqueue(emptyResponse.setResponseCode(HttpURLConnection.HTTP_OK))

        val response = repositoryService.fetchRepos()
        if (response is ApiResponse.Success) {
            val users = response.data
            assertNotNull(users)
            assertTrue(users.isEmpty())
        }
    }
}
