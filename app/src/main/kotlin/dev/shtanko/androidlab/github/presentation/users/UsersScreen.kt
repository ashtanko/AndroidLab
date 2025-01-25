@file:Suppress("TooManyFunctions")

package dev.shtanko.androidlab.github.presentation.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.presentation.model.UserResource
import dev.shtanko.androidlab.github.presentation.preview.UsersPreviewDataProvider
import dev.shtanko.androidlab.github.presentation.shared.CircularAvatarImage
import dev.shtanko.androidlab.github.presentation.shared.EmptyContent
import dev.shtanko.androidlab.github.presentation.shared.ErrorContent
import dev.shtanko.androidlab.github.presentation.shared.LoadingContent
import dev.shtanko.androidlab.github.presentation.shared.PullToRefresh
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun UsersScreen(
    modifier: Modifier = Modifier,
    onUserClick: (Int) -> Unit = {},
    viewModel: UserViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    UsersContent(
        uiState = uiState,
        modifier = modifier,
        isRefreshing = isRefreshing,
        onTryAgainClick = { viewModel.retry() },
        onUserClick = onUserClick,
        onRefresh = { viewModel.refresh() },
    )
}

@Composable
fun UsersContent(
    uiState: UserUiState,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    onUserClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    when (uiState) {
        UserUiState.Error -> ErrorContent(modifier = modifier, onTryAgainClick)
        UserUiState.Loading -> LoadingContent(modifier = modifier)
        is UserUiState.Success -> UsersListContent(
            users = uiState.users,
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            onUserClick = onUserClick,
        )

        UserUiState.Empty -> EmptyContent(
            modifier = modifier,
            content = stringResource(R.string.empty_users_title),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersListContent(
    users: ImmutableList<UserResource>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onUserClick: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 0,
    )

    PullToRefresh(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        testTag = "PullToRefreshBox",
        testIndicatorTag = "PullToRefreshIndicator",
        content = {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("user_list"),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(
                    users.size,
                    key = { index -> users[index].id + index },
                ) { index ->
                    val item = users[index]
                    UserItem(
                        item,
                        modifier = Modifier
                            .animateItem()
                            .clickable(
                                onClick = {
                                    onUserClick.invoke(item.id)
                                },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                ),
                            ),
                    )
                }
            }
        },
    )
}

@Composable
private fun UserItem(
    user: UserResource,
    modifier: Modifier = Modifier,
) = trace("user_item") {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularAvatarImage(
            sizeDp = 32.dp,
            imageUrl = user.avatarUrl,
        )
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 8.dp),
            text = user.login,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersListContentPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Success(
                users = preview,
            ),
            isRefreshing = false,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersListContentRefreshingPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Success(
                users = preview,
            ),
            isRefreshing = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersErrorContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Error,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersLoadingContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsersEmptyContentPreview() {
    AndroidLabTheme {
        UsersContent(
            uiState = UserUiState.Empty,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UserItemPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UserItem(
            user = preview.first(),
        )
    }
}
