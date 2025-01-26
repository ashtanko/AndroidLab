package dev.shtanko.androidlab.github.data.repository.repos

import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.repository.OfflineFirstRepositoriesRepository
import dev.shtanko.androidlab.github.data.repository.RepositoriesRepository
import dev.shtanko.androidlab.github.data.service.RepositoryService
import dev.shtanko.androidlab.util.MainDispatcherRule
import org.junit.Rule
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class RepositoriesRepositoryTest {
    private val db: GithubDatabase = Mockito.mock()
    private val service: RepositoryService = Mockito.mock()
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
        Assertions.assertNotNull(repository)
    }
}
