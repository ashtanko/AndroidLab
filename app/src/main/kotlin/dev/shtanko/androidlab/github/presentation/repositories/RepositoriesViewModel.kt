package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.shtanko.androidlab.github.data.repository.RepositoriesRepository
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val repository: RepositoriesRepository,
) : ViewModel() {

    private val fetchTrigger = MutableSharedFlow<Boolean>(replay = 1)

    val uiState: StateFlow<PagingData<RepositoryResource>> = fetchTrigger.flatMapLatest {
        repository.getRepos().cachedIn(viewModelScope)
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

@Stable
sealed interface RepositoriesUiState {
    data object Loading : RepositoriesUiState

    data class Success(val repositories: Flow<PagingData<RepositoryResource>>) : RepositoriesUiState

    data object Error : RepositoriesUiState

    data object Empty : RepositoriesUiState
}
