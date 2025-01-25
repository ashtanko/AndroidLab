@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.shared.CircularAvatarImage
import dev.shtanko.androidlab.github.presentation.shared.PullToRefresh
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.flowOf

@Composable
fun RepositoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RepositoriesViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsLazyPagingItems()

    RepositoriesScreen(
        modifier = modifier,
        uiState = uiState,
        isRefreshing = false,
        onRefresh = { viewModel.refresh() },
    )
}

@Composable
fun RepositoriesScreen(
    uiState: LazyPagingItems<RepositoryResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    onClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    RepositoriesList(
        pagingItems = uiState,
        isRefreshing = isRefreshing,
        modifier = modifier,
        onClick = onClick,
        onRefresh = onRefresh,
    )
}

@Composable
private fun RepositoriesList(
    pagingItems: LazyPagingItems<RepositoryResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
    )
    PullToRefresh(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        testTag = "RepositoriesPullToRefreshBox",
        testIndicatorTag = "RepositoriesPullToRefreshIndicator",
        content = {
            LazyColumn(
                state = listState,
                modifier = Modifier,
                content = {
                    items(
                        count = pagingItems.itemCount,
                        key = pagingItems.itemKey { it.id },
                        contentType = pagingItems.itemContentType { "Item" },
                    ) { i ->

                        val item = pagingItems[i]
                        item?.let {
                            RepositoryItem(
                                item = it,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = { onClick(it.id) }),
                            )
                        }
                    }
                },
            )
        },
    )
}

@Composable
fun RepositoryItem(
    item: RepositoryResource,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularAvatarImage(
                sizeDp = 16.dp,
                borderDp = 0.dp,
                imageUrl = item.owner?.avatarUrl,
            )
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp),
                text = item.owner?.login.orEmpty(),
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier,
                text = item.name.orEmpty(),
            )
        }
        item.description.takeIf { it?.isNotEmpty() == true }?.let { description ->
            Text(
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier,
                text = description,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                )
                Text(
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier,
                    text = item.stars.toString(),
                )
            }
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp),
                text = item.language.orEmpty(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoriesScreenItemsPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
) {
    AndroidLabTheme {
        val pagingData = PagingData.from(preview)
        RepositoriesScreen(
            uiState = flowOf(pagingData).collectAsLazyPagingItems(),
            isRefreshing = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RepositoryItemPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: ImmutableList<RepositoryResource>,
) {
    AndroidLabTheme {
        RepositoryItem(item = preview.first())
    }
}
