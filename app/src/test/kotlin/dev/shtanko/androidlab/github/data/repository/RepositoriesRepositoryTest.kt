package dev.shtanko.androidlab.github.data.repository

import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.service.RepositoryService
import dev.shtanko.androidlab.util.MainDispatcherRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class RepositoriesRepositoryTest {
    private val db: GithubDatabase = mock()
    private val service: RepositoryService = mock()
    private lateinit var repository: RepositoriesRepository

    @get:Rule
    val coroutinesRule = MainDispatcherRule()

    @BeforeEach
    fun setup() {
        repository = OfflineFirstRepositoriesRepository(
            service = service,
            db = db,
            ioDispatcher = coroutinesRule.testDispatcher,
        )
    }

    @Test
    fun `repository instance should not be null`() {
        assertNotNull(repository)
    }
}
