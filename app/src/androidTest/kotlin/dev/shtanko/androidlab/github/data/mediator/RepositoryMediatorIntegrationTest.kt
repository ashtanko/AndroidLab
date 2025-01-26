package dev.shtanko.androidlab.github.data.mediator

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator.MediatorResult
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.db.dao.RepositoryDao
import dev.shtanko.androidlab.github.data.db.dao.RepositoryRemoteKeysDao
import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.model.NetworkRepository
import dev.shtanko.androidlab.github.data.model.NetworkUser
import dev.shtanko.androidlab.github.data.service.RepositoryService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response

@ExperimentalPagingApi
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RepositoryMediatorIntegrationTest {
    private lateinit var db: GithubDatabase
    private lateinit var repositoryDao: RepositoryDao
    private lateinit var repositoryRemoteKeysDao: RepositoryRemoteKeysDao
    private val service: RepositoryService = mockk(relaxed = true)

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                GithubDatabase::class.java,
            ).build()
        }
        repositoryDao = db.repositoryDao()
        repositoryRemoteKeysDao = db.repositoryRemoteKeysDao()
    }

    @After
    fun teardown() = db.close()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val mockRepos = List(10) {
            NetworkRepository(
                id = it + 1,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=${it + 1}",
                name = "Sample Repo${it + 1}",
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
                    id = it + 1,
                    login = "sample${it + 1}",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                ),
            )
        }
        val mockResponse = ApiResponse.responseOf {
            Response.success(mockRepos)
        }
        coEvery { service.fetchRepos(any(), any(), any()) } returns mockResponse
        val remoteMediator = RepositoryMediator(
            service = service,
            database = db,
            username = "google",
        )
        val pagingState = PagingState<Int, RepositoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertFalse((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val mockResponse = ApiResponse.responseOf {
            Response.success(emptyList<NetworkRepository>())
        }
        coEvery { service.fetchRepos(any(), any(), any()) } returns mockResponse
        val remoteMediator = RepositoryMediator(
            service = service,
            database = db,
            username = "google",
        )
        val pagingState = PagingState<Int, RepositoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Success)
        assertTrue((result as MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        val mockResponse = ApiResponse.responseOf {
            Response.error<List<NetworkRepository>>(
                400,
                "No data found".toResponseBody(null),
            )
        }
        coEvery { service.fetchRepos(any(), any(), any()) } returns mockResponse
        val remoteMediator = RepositoryMediator(
            service = service,
            database = db,
            username = "google",
        )
        val pagingState = PagingState<Int, RepositoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10,
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is MediatorResult.Error)
    }
}
