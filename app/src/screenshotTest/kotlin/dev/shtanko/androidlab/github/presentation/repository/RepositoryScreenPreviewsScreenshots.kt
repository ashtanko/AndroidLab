package dev.shtanko.androidlab.github.presentation.repository

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesTopAppBar
import dev.shtanko.androidlab.github.presentation.repositories.RepositoryItem
import dev.shtanko.androidlab.github.presentation.repositories.RepositoryItemLoading
import dev.shtanko.androidlab.github.presentation.repositories.RepositoryNoMoreItems
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class RepositoryScreenPreviewsScreenshots {
    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoryNoMoreItemsPreview() {
        AndroidLabTheme {
            RepositoryNoMoreItems()
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoryItemLoadingPreview() {
        AndroidLabTheme {
            RepositoryItemLoading()
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesTopAppBarPreview() {
        AndroidLabTheme {
            RepositoriesTopAppBar()
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesScreenItemsPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview)
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                uiState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesScreenItemsLoadingPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
    ) {
        AndroidLabTheme {
            val pagingData = PagingData.from(preview)
            val flow = flowOf(pagingData)
            RepositoriesScreen(
                uiState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesScreenItemsEmptyPreview() {
        AndroidLabTheme {
            val pagingData = PagingData.empty<RepositoryResource>()
            val flow = MutableStateFlow(pagingData)
            RepositoriesScreen(
                uiState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoriesScreenItemsErrorPreview() {
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
                uiState = flow.collectAsLazyPagingItems(),
                isRefreshing = false,
            )
        }
    }

    @Preview(
        name = "Light Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_NO,
    )
    @Preview(
        name = "Dark Mode",
        showBackground = true,
        uiMode = Configuration.UI_MODE_NIGHT_YES,
    )
    @Composable
    private fun RepositoryItemPreview(
        @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
    ) {
        AndroidLabTheme {
            RepositoryItem(item = preview.first())
        }
    }
}
