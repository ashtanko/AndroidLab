package dev.shtanko.androidlab.github.presentation.users

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.androidlab.github.data.repository.UserRepository
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val fetchTrigger = MutableSharedFlow<Boolean>(replay = 1)
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<UserUiState> = fetchTrigger.flatMapLatest { force ->
        _isRefreshing.value = true
        userRepository.fetchUsers(force = force).map {
            _isRefreshing.value = false
            if (it.isSuccess) {
                val list = it.getOrNull() ?: emptyList()
                if (list.isEmpty()) {
                    UserUiState.Empty
                } else {
                    UserUiState.Success(list.toImmutableList())
                }
            } else {
                UserUiState.Error
            }
        }.onCompletion {
            _isRefreshing.value = false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserUiState.Loading,
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
sealed interface UserUiState {

    data object Loading : UserUiState

    data class Success(val users: ImmutableList<UserResource>) : UserUiState

    data object Error : UserUiState

    data object Empty : UserUiState
}
