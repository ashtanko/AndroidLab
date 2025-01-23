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

    private lateinit var viewModel: UserViewModel
    private var userRepository = FakeUserRepository()

    @BeforeEach
    fun setup() {
        viewModel = UserViewModel(
            userRepository,
        )
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
        }
    }

    @Test
    fun `fetchUsers emits Success state when repository fetch succeeds`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        userRepository.sendUserResources(mockSuccessfulResult)
        viewModel.retry()

        viewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            val successState = awaitItem() as UserUiState.Success
            assertEquals(mockUsers.toImmutableList(), successState.users)
        }
    }

    @Test
    fun `fetchUsers emits Error state when repository fetch fails`() = runTest {
        userRepository.sendUserResources(Result.failure(Exception("Network error")))
        viewModel.retry()

        viewModel.uiState.test {
            assertEquals(UserUiState.Loading, awaitItem())
            assertEquals(UserUiState.Error, awaitItem())
        }
    }

    @Test
    @Disabled("This test is flaky")
    fun `refresh sets isRefreshing to true during fetch`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        userRepository.sendUserResources(mockSuccessfulResult)

        viewModel.isRefreshing.test(timeout = 3.seconds) {
            assertEquals(false, awaitItem())
            viewModel.refresh()
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `fetchUsers emits Empty state when no users are fetched`() = runTest {
        userRepository.sendUserResources(Result.success(emptyList()))
        viewModel.retry()

        viewModel.uiState.test {
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

    private val mockSuccessfulResult = Result.success(mockUsers)
}
