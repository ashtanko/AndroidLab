package dev.shtanko.androidlab.github.presentation

import app.cash.turbine.test
import app.cash.turbine.turbineScope
import dev.shtanko.androidlab.github.data.repository.FakeUserRepository
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.util.MainDispatcherRule
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userViewModel: UserViewModel
    private val fakeUserRepository = FakeUserRepository()

    @BeforeEach
    fun initializeViewModel() {
        userViewModel = UserViewModel(fakeUserRepository)
    }

    @Test
    fun `uiState should initially be Loading`() = runTest {
        turbineScope {
            userViewModel.uiState.test {
                assertEquals(UserUiState.Loading, awaitItem())
            }
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `retry fetch emits Success state when repository returns users`() = runTest {
        fakeUserRepository.emitUserResources(mockSuccessResult)
        userViewModel.retry()

        advanceUntilIdle()
        userViewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            val successState = awaitItem() as UserUiState.Success
            assertEquals(mockUsers.toImmutableList(), successState.users)
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `retry fetch emits Error state when repository fetch fails`() = runTest {
        turbineScope {
            fakeUserRepository.emitUserResources(Result.failure(Exception("Network error")))
            userViewModel.retry()

            advanceUntilIdle()
            userViewModel.uiState.test {
                assertEquals(UserUiState.Loading, awaitItem())
                assertEquals(UserUiState.Error, awaitItem())
            }
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `refresh updates isRefreshing state correctly`() = runTest {
        turbineScope {
            backgroundScope.launch(UnconfinedTestDispatcher()) { userViewModel.uiState.collect() }

            fakeUserRepository.emitUserResources(mockSuccessResult)
            advanceUntilIdle()
            userViewModel.isRefreshing.test {
                assertFalse(awaitItem())
                userViewModel.refresh()
                assertTrue(awaitItem())
                assertFalse(awaitItem())
                cancelAndConsumeRemainingEvents()
            }
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `retry fetch emits Empty state when no users are fetched`() = runTest {
        turbineScope {
            fakeUserRepository.emitUserResources(Result.success(emptyList()))
            userViewModel.retry()

            advanceUntilIdle()
            userViewModel.uiState.test {
                assertEquals(UserUiState.Loading, awaitItem())
                assertEquals(UserUiState.Empty, awaitItem())
            }
        }
    }

    private val mockUsers = listOf(
        UserResource(
            id = 1,
            login = "alex",
            avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
        ),
    )

    private val mockSuccessResult = Result.success(mockUsers)
}
