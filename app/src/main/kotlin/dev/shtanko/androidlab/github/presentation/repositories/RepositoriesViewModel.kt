package dev.shtanko.androidlab.github.presentation.repositories

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.androidlab.github.GithubScreenRoutes
import dev.shtanko.androidlab.github.data.repository.RepositoriesRepository
import dev.shtanko.androidlab.github.data.repository.UserRepository
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val repository: RepositoriesRepository,
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val username: String =
        savedStateHandle.get<String>(GithubScreenRoutes.ARG_USERNAME).orEmpty()

    private val fetchTrigger = MutableSharedFlow<Boolean>(replay = 1)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val repositoriesState: StateFlow<PagingData<RepositoryResource>> = fetchTrigger.flatMapLatest {
        repository.getRepos(username = username).cachedIn(viewModelScope)
            .onStart {
                Log.d("RepositoriesViewModel", "repositories onStart")
            }
            .onCompletion {
                Log.d("RepositoriesViewModel", "repositories onCompletion")
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PagingData.empty(),
    )

    val userState: StateFlow<RepositoriesUiState> = fetchTrigger.flatMapLatest {
        userRepository.getFullUser(login = username).map { user ->
            if (user.isSuccess) {
                RepositoriesUiState.Success(user.getOrNull()!!)
            } else {
                RepositoriesUiState.Error
            }
        }.onStart {
            Log.d("RepositoriesViewModel", "onStart")
            _isRefreshing.value = true
        }.onCompletion {
            Log.d("RepositoriesViewModel", "onCompletion")
            _isRefreshing.value = false
        }
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
    ) : RepositoriesUiState

    data object Error : RepositoriesUiState
}
