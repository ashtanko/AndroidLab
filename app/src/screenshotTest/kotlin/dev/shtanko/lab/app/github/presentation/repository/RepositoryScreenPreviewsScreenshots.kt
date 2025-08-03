package dev.shtanko.lab.app.github.presentation.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.R
import dev.shtanko.lab.app.github.presentation.model.RepositoryResource
import dev.shtanko.lab.app.github.presentation.model.UserFullResource
import dev.shtanko.lab.app.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesTopAppBar
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesUiState
import dev.shtanko.lab.app.github.presentation.repositories.UserDetailsHeaderItem
import dev.shtanko.lab.app.github.presentation.repositories.UserDetailsItemIconInfo
import dev.shtanko.lab.app.utils.ThemePreviews
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow

class RepositoryScreenPreviewsScreenshots {
    @ThemePreviews
    @Composable
    private fun UserDetailsItemIconInfoPreview() {
        AndroidLabTheme {
            UserDetailsItemIconInfo(
                iconRes = R.drawable.ic_group,
                text = "150",
            )
        }
    }

    @ThemePreviews
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

    @ThemePreviews
    @Composable
    private fun RepositoriesTopAppBarPreview() {
        AndroidLabTheme {
            RepositoriesTopAppBar()
        }
    }

    @ThemePreviews
    @Composable
    private fun RepositoriesScreenItemsPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview.second)
            val repositories = MutableStateFlow(pagingData)
            RepositoriesScreen(
                uiState = RepositoriesUiState.Success(
                    user = preview.first,
                    repositories = repositories,
                ),
                isRefreshing = false,
            )
        }
    }

    @ThemePreviews
    @Composable
    private fun RepositoriesScreenItemsLoadingPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            RepositoriesScreen(
                uiState = RepositoriesUiState.Loading,
                isRefreshing = false,
            )
        }
    }

    @ThemePreviews
    @Composable
    private fun RepositoriesScreenItemsEmptyPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.empty<RepositoryResource>()
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                uiState = RepositoriesUiState.Success(
                    user = preview.first,
                    repositories = flow,
                ),
                isRefreshing = false,
            )
        }
    }

    @ThemePreviews
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
                uiState = RepositoriesUiState.Success(
                    user = preview.first,
                    repositories = flow,
                ),
                isRefreshing = false,
            )
        }
    }
}
