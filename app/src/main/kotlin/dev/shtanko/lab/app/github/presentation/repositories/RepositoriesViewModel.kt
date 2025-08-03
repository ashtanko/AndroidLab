package dev.shtanko.lab.app.github.presentation.repositories

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.lab.app.github.GithubScreenRoutes
import dev.shtanko.lab.app.github.data.repository.RepositoriesRepository
import dev.shtanko.lab.app.github.data.repository.UserRepository
import dev.shtanko.lab.app.github.presentation.model.RepositoryResource
import dev.shtanko.lab.app.github.presentation.model.UserFullResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    repository: RepositoriesRepository,
    userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val username: String =
        savedStateHandle.get<String>(GithubScreenRoutes.Companion.ARG_USERNAME).orEmpty()

    private val fetchTrigger = MutableSharedFlow<Boolean>(replay = 1)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val repositoriesUiState: StateFlow<RepositoriesUiState> =
        combine(
            fetchTrigger,
            userRepository.getFullUser(login = username),
            repository.getRepos(username = username).cachedIn(viewModelScope),
        ) { _, user, repos ->
            if (user.isSuccess) {
                RepositoriesUiState.Success(user.getOrNull()!!, flowOf(repos))
            } else {
                RepositoriesUiState.Error
            }
        }.onStart {
            _isRefreshing.value = true
            Log.d("TEST_TEST", "onStart")
        }.onCompletion {
            Log.d("TEST_TEST", "onCompletion")
            _isRefreshing.value = false
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepositoriesUiState.Loading,
        )

    init {
        fetchTrigger.tryEmit(false)
    }

    fun refresh() {
        viewModelScope.launch {
            fetchTrigger.emit(true)
        }
    }

    fun retry() {
        viewModelScope.launch {
            fetchTrigger.emit(false)
        }
    }
}

@Stable
sealed interface RepositoriesUiState {
    data object Loading : RepositoriesUiState

    data class Success(
        val user: UserFullResource,
        val repositories: Flow<PagingData<RepositoryResource>>,
    ) : RepositoriesUiState

    data object Error : RepositoriesUiState
}
