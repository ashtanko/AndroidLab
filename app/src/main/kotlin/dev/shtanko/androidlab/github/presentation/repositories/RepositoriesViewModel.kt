package dev.shtanko.androidlab.github.presentation.repositories

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.androidlab.github.GithubScreenRoutes
import dev.shtanko.androidlab.github.data.repository.RepositoriesRepository
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val repository: RepositoriesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val username: String =
        savedStateHandle.get<String>(GithubScreenRoutes.ARG_USERNAME).orEmpty()

    private val fetchTrigger = MutableSharedFlow<Boolean>(replay = 1)

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val uiState: StateFlow<PagingData<RepositoryResource>> = fetchTrigger.flatMapLatest {
        _isRefreshing.value = true
        repository.getRepos(username = username).cachedIn(viewModelScope).onCompletion {
            _isRefreshing.value = false
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PagingData.empty(),
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
