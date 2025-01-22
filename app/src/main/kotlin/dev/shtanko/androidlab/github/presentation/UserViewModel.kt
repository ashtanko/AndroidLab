package dev.shtanko.androidlab.github.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.androidlab.github.data.repository.UserRepository
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val uiState: StateFlow<UserUiState> =
        MutableStateFlow<UserUiState>(UserUiState.Loading).flatMapLatest {
            userRepository.fetchUsers().distinctUntilChanged().map {
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
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserUiState.Loading,
        )

    var isRefreshing: StateFlow<Boolean> = uiState.map {
        it == UserUiState.Loading
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = true,
    )

    fun refresh() {
        userRepository.fetchUsers(force = true)
        (uiState as? MutableStateFlow)?.value = UserUiState.Loading
    }

    fun retry() {
        (uiState as? MutableStateFlow)?.value = UserUiState.Loading
    }
}

sealed interface UserUiState {

    data object Loading : UserUiState

    data class Success(val users: ImmutableList<UserResource>) : UserUiState

    data object Error : UserUiState

    data object Empty : UserUiState
}
