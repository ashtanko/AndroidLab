package dev.shtanko.androidlab.github.presentation.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesTopAppBar
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesUiState
import dev.shtanko.androidlab.github.presentation.repositories.UserDetailsHeaderItem
import dev.shtanko.androidlab.github.presentation.repositories.UserDetailsItemIconInfo
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class RepositoryScreenPreviewsScreenshots {
    @ThemesPreviews
    @Composable
    private fun UserDetailsItemIconInfoPreview() {
        AndroidLabTheme {
            UserDetailsItemIconInfo(
                iconRes = R.drawable.ic_group,
                text = "150",
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun UserDetailsHeaderItemPreview() {
        AndroidLabTheme {
            UserDetailsHeaderItem(
                user = UserFullResource(
                    id = 1,
                    login = "login",
                    avatarUrl = "https://avatars.githubusercontent.com/u/1?v=4",
                    name = "Name",
                    company = "Company",
                    bio = LoremIpsum(100).values.first(),
                ),
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoriesTopAppBarPreview() {
        AndroidLabTheme {
            RepositoriesTopAppBar()
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoriesScreenItemsPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview.second)
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                userState = RepositoriesUiState.Success(
                    user = preview.first,
                ),
                repositoriesState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoriesScreenItemsLoadingPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview.second)
            val flow = flowOf(pagingData)
            RepositoriesScreen(
                userState = RepositoriesUiState.Success(
                    user = preview.first,
                ),
                repositoriesState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoriesScreenItemsEmptyPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.empty<RepositoryResource>()
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                userState = RepositoriesUiState.Success(
                    user = preview.first,
                ),
                repositoriesState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @ThemesPreviews
    @Composable
    private fun RepositoriesScreenItemsErrorPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(
                data = emptyList<RepositoryResource>(),
                sourceLoadStates = LoadStates(
                    LoadState.Error(Exception("Error")),
                    LoadState.Error(Exception("Error")),
                    LoadState.Error(Exception("Error")),
                ),
            )
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                userState = RepositoriesUiState.Success(
                    user = preview.first,
                ),
                repositoriesState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }
}
