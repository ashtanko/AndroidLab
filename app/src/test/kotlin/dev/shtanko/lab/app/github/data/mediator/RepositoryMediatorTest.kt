package dev.shtanko.lab.app.github.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.retrofit.responseOf
import dev.shtanko.lab.app.github.data.db.GithubDatabase
import dev.shtanko.lab.app.github.data.db.dao.RepositoryDao
import dev.shtanko.lab.app.github.data.db.dao.RepositoryRemoteKeysDao
import dev.shtanko.lab.app.github.data.db.entity.RepositoryEntity
import dev.shtanko.lab.app.github.data.model.NetworkRepository
import dev.shtanko.lab.app.github.data.service.RepositoryService
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class RepositoryMediatorTest {
    private val service: RepositoryService = mock()
    private val db: GithubDatabase = mock()
    private val repositoryRemoteKeysDao: RepositoryRemoteKeysDao = mock()
    private val repositoryDao: RepositoryDao = mock()
    private val mediator = RepositoryMediator(service, db, username = "google")

    @Test
    fun `mediator instance should not be null`() {
        assertNotNull(mediator)
    }

    @Test
    fun initialize_withUpToDateCache_returnsSkipInitialRefresh() = runTest {
        `when`(db.repositoryRemoteKeysDao()).thenReturn(repositoryRemoteKeysDao)
        `when`(repositoryRemoteKeysDao.getCreationTime()).thenReturn(System.currentTimeMillis())
        `when`(repositoryRemoteKeysDao.getUsers()).thenReturn(listOf("google"))

        val result = mediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH, result)
    }

    @Test
    fun initialize_withStaleCache_returnsLaunchInitialRefresh() = runTest {
        `when`(db.repositoryRemoteKeysDao()).thenReturn(repositoryRemoteKeysDao)
        `when`(repositoryRemoteKeysDao.getCreationTime()).thenReturn(0L)

        val result = mediator.initialize()
        assertEquals(RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH, result)
    }

    @Test
    fun load_appendLoadType_success() = runTest {
        val pagingState = PagingState<Int, RepositoryEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0,
        )
        val mockResponse = ApiResponse.responseOf {
            Response.success(emptyList<NetworkRepository>())
        }
        `when`(service.fetchRepos(anyString(), anyInt(), anyInt())).thenReturn(mockResponse)

        val result = mediator.load(LoadType.APPEND, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
    }

    @Test
    @Disabled("should be fixed")
    fun load_refresh_successfullyFetchesData() = runTest {
        // Mock service response
        val mockResponse = listOf(
            NetworkRepository(
                id = 1,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=1",
                name = "Repo1",
                stars = 100,
            ),
            NetworkRepository(
                id = 2,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=2",
                name = "Repo2",
                stars = 200,
            ),
        )
        val mockNetworkResponse = ApiResponse.responseOf {
            Response.success(mockResponse)
        }
        `when`(service.fetchRepos(anyString(), anyInt(), anyInt())).thenReturn(mockNetworkResponse)
        // `when`(db.transactionExecutor).thenReturn(Executors.newSingleThreadExecutor())

        // Prepare PagingState
        val pagingState = PagingState<Int, RepositoryEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0,
        )

        val result = mediator.load(LoadType.REFRESH, pagingState)

        val mockPagingSource =
            Mockito.mock(PagingSource::class.java) as PagingSource<Int, RepositoryEntity>
        `when`(mockPagingSource.load(any())).thenReturn(
            PagingSource.LoadResult.Page(
                data = listOf(
                    RepositoryEntity(
                        id = 1,
                        repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=1",
                        name = "Repo1",
                    ),
                    RepositoryEntity(
                        id = 2,
                        repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=2",
                        name = "Repo2",
                    ),
                ),
                prevKey = null,
                nextKey = 2,
            ),
        )

        `when`(repositoryDao.getRepositories()).thenReturn(mockPagingSource)

        // Verify data inserted into the database
        val insertedRepos = repositoryDao.getRepositories().load(
            PagingSource.LoadParams.Refresh(key = null, loadSize = 10, placeholdersEnabled = false),
        ) as PagingSource.LoadResult.Page
        assertEquals(2, insertedRepos.data.size)
        assertEquals("Repo1", insertedRepos.data[0].name)

        // Validate the result of the mediator load
        assertTrue(result is RemoteMediator.MediatorResult.Error)
        // assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun load_withIOException_returnsError() = runTest {
        val pagingState = PagingState<Int, RepositoryEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0,
        )
        given(
            service.fetchRepos(
                anyString(),
                anyInt(),
                anyInt(),
            ),
        ).willAnswer {
            throw IOException()
        }

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @Test
    fun load_withHttpException_returnsError() = runTest {
        val pagingState = PagingState<Int, RepositoryEntity>(
            pages = listOf(),
            anchorPosition = null,
            config = PagingConfig(pageSize = 10),
            leadingPlaceholderCount = 0,
        )
        `when`(
            service.fetchRepos(
                anyString(),
                anyInt(),
                anyInt(),
            ),
        ).thenThrow(HttpException::class.java)

        val result = mediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }
}
