package dev.shtanko.androidlab.github.data.dao

import androidx.paging.PagingSource
import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shtanko.androidlab.github.data.db.DatabaseTest
import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.db.entity.RepositoryRemoteKeysEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryDaoTest : DatabaseTest() {
    @Test
    fun insertAndFetchRepositoriesWithPaging() = runTest {
        val repositories = listOf(
            RepositoryEntity(
                id = 1,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=1",
                name = "Repo1",
                fullName = "User/Repo1",
                stars = 50,
            ),
            RepositoryEntity(
                id = 2,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=2",
                name = "Repo2",
                fullName = "User/Repo2",
                stars = 100,
            ),
            RepositoryEntity(
                id = 3,
                repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=3",
                name = "Repo3",
                fullName = "User/Repo3",
                stars = 150,
            ),
        )

        // Insert repositories into the database
        repositoryDao.insertAll(repositories)

        // Fetch PagingSource and collect the data
        val pagingSource = repositoryDao.getRepositories()
        val results = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false,
            ),
        )

        // Validate PagingSource results
        assertTrue(results is PagingSource.LoadResult.Page)
        val page = results as PagingSource.LoadResult.Page
        assertEquals(3, page.data.size)

        assertEquals(repositories[0], page.data[0])
        assertEquals(repositories[1], page.data[1])
        assertEquals(repositories[2], page.data[2])
    }

    @Test
    fun insertDuplicateRepositoriesAndValidate() = runTest {
        val repo1 = RepositoryEntity(
            id = 1,
            repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=1",
            name = "Repo1",
            fullName = "User/Repo1",
            stars = 50,
        )

        val repo1Updated = RepositoryEntity(
            id = 1,
            repositoryId = "MDEwOlJlcG9zaXRvcnkzMjAwNDEwNzE=2",
            name = "UpdatedRepo1",
            fullName = "User/UpdatedRepo1",
            stars = 100,
        )

        // Insert the original repository
        repositoryDao.insertAll(listOf(repo1))

        // Insert the updated repository with the same ID
        repositoryDao.insertAll(listOf(repo1Updated))

        // Retrieve and validate the updated data
        val updatedRepo = db.query("SELECT * FROM repos WHERE id = 1", null)

        assertTrue(updatedRepo.moveToFirst())
        assertEquals(1, updatedRepo.getInt(updatedRepo.getColumnIndexOrThrow("id")))
        assertEquals(
            "UpdatedRepo1",
            updatedRepo.getString(updatedRepo.getColumnIndexOrThrow("name")),
        )
        assertEquals(100, updatedRepo.getInt(updatedRepo.getColumnIndexOrThrow("stargazers_count")))

        updatedRepo.close()
    }
}

@RunWith(AndroidJUnit4::class)
class RepositoryRemoteKeysDaoTest : DatabaseTest() {
    @Test
    fun insertAndRetrieveRemoteKeyById() = runTest {
        val remoteKey = RepositoryRemoteKeysEntity(
            repositoryId = "repo1",
            user = "user1",
            prevKey = null,
            currentPage = 1,
            nextKey = 2,
        )

        repositoryRemoteKeysDao.insertAll(listOf(remoteKey))
        val retrievedKey = repositoryRemoteKeysDao.getRemoteKeyById("repo1")

        assertNotNull(retrievedKey)
        assertEquals("repo1", retrievedKey?.repositoryId)
        assertEquals(1, retrievedKey?.currentPage)
        assertEquals(2, retrievedKey?.nextKey)
        assertNull(retrievedKey?.prevKey)
    }

    @Test
    fun retrieveLatestCreationTime() = runTest {
        val remoteKeys = listOf(
            RepositoryRemoteKeysEntity(
                repositoryId = "repo1",
                user = "user1",
                prevKey = null,
                currentPage = 1,
                nextKey = 2,
                createdAt = 1000L,
            ),
            RepositoryRemoteKeysEntity(
                repositoryId = "repo2",
                user = "user1",
                prevKey = 1,
                currentPage = 2,
                nextKey = 3,
                createdAt = 2000L,
            ),
        )

        repositoryRemoteKeysDao.insertAll(remoteKeys)
        val latestCreationTime = repositoryRemoteKeysDao.getCreationTime()

        assertNotNull(latestCreationTime)
        assertEquals(2000L, latestCreationTime)
    }

    @Test
    fun retrieveUsers() = runTest {
        val remoteKeys = listOf(
            RepositoryRemoteKeysEntity(
                repositoryId = "repo1",
                user = "user1",
                prevKey = null,
                currentPage = 1,
                nextKey = 2,
                createdAt = 1000L,
            ),
            RepositoryRemoteKeysEntity(
                repositoryId = "repo2",
                user = "user2",
                prevKey = 1,
                currentPage = 2,
                nextKey = 3,
                createdAt = 2000L,
            ),
        )

        repositoryRemoteKeysDao.insertAll(remoteKeys)
        val users = repositoryRemoteKeysDao.getUsers()

        assertNotNull(users)
        assertEquals(listOf("user1", "user2"), users)
    }

    @Test
    fun clearRemoteKeys() = runTest {
        val remoteKeys = listOf(
            RepositoryRemoteKeysEntity(
                repositoryId = "repo1",
                user = "user1",
                prevKey = null,
                currentPage = 1,
                nextKey = 2,
            ),
            RepositoryRemoteKeysEntity(
                repositoryId = "repo2",
                user = "user2",
                prevKey = 1,
                currentPage = 2,
                nextKey = 3,
            ),
        )

        repositoryRemoteKeysDao.insertAll(remoteKeys)
        val retrievedKeyBeforeClean = repositoryRemoteKeysDao.getRemoteKeyById("repo1")
        assertNotNull(retrievedKeyBeforeClean)
        repositoryRemoteKeysDao.clearRemoteKeys()

        val retrievedKey = repositoryRemoteKeysDao.getRemoteKeyById("repo1")
        val latestCreationTime = repositoryRemoteKeysDao.getCreationTime()

        assertNull(retrievedKey)
        assertNull(latestCreationTime)
    }
}
