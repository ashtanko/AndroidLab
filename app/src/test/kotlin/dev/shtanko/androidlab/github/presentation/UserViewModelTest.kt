package dev.shtanko.androidlab.github.presentation

import dev.shtanko.androidlab.github.data.repository.FakeUserRepository
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf

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
    fun stateIsInitiallyLoading() = runTest {
        assertEquals(UserUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun oneUser_showsInUI() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        val mockUsers = listOf(
            UserResource(
                id = 1,
                login = "alex",
                avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                type = "User",
            ),
        )
        runBlocking {
            userRepository.sendUserResources(Result.success(mockUsers))
            delay(100L) // todo
            val item = viewModel.uiState.value
            assertInstanceOf<UserUiState.Success>(item)
        }
    }
}
