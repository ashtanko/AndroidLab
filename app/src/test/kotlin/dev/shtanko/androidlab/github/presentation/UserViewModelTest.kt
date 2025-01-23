package dev.shtanko.androidlab.github.presentation

import app.cash.turbine.test
import dev.shtanko.androidlab.github.data.repository.FakeUserRepository
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.util.MainDispatcherRule
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.seconds

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
        userViewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `retry fetch emits Success state when repository returns users`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { userViewModel.uiState.collect() }
        fakeUserRepository.emitUserResources(mockSuccessResult)
        userViewModel.retry()

        userViewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            val successState = awaitItem() as UserUiState.Success
            assertEquals(mockUsers.toImmutableList(), successState.users)
        }
    }

    @Test
    fun `retry fetch emits Error state when repository fetch fails`() = runTest {
        fakeUserRepository.emitUserResources(Result.failure(Exception("Network error")))
        userViewModel.retry()

        userViewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            assertEquals(UserUiState.Error, awaitItem())
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `refresh updates isRefreshing state correctly`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { userViewModel.uiState.collect() }
        fakeUserRepository.emitUserResources(mockSuccessResult)

        userViewModel.isRefreshing.test(timeout = 3.seconds) {
            assertEquals(false, awaitItem())
            userViewModel.refresh()
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `retry fetch emits Empty state when no users are fetched`() = runTest {
        fakeUserRepository.emitUserResources(Result.success(emptyList()))
        userViewModel.retry()

        userViewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            assertEquals(UserUiState.Empty, awaitItem())
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
