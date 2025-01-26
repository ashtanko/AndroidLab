@file:Suppress("TooManyFunctions")

package dev.shtanko.androidlab.github.presentation.users

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import androidx.window.core.layout.WindowSizeClass
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.github.designsystem.ScreenBackground
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
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    onUserClick: (String) -> Unit = {},
    viewModel: UserViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    ScreenBackground(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) { contentPadding ->
            UsersContent(
                uiState = uiState,
                modifier = Modifier.padding(contentPadding),
                isRefreshing = isRefreshing,
                onTryAgainClick = { viewModel.retry() },
                onUserClick = onUserClick,
                onRefresh = { viewModel.refresh() },
            )
        }
    }
}

@Composable
fun UsersContent(
    uiState: UserUiState,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onTryAgainClick: () -> Unit = {},
    onUserClick: (String) -> Unit = {},
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
    onUserClick: (String) -> Unit = {},
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
                        onUserClick = onUserClick,
                        modifier = Modifier
                            .animateItem()
                            .fillMaxWidth(),
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
    onUserClick: (String) -> Unit = {},
) = trace("user_item") {
    Box(modifier = modifier.padding(vertical = 2.dp, horizontal = 12.dp)) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                onUserClick.invoke(user.login)
            },
            modifier = modifier,
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularAvatarImage(
                    sizeDp = 32.dp,
                    imageUrl = user.avatarUrl,
                )
                Text(
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 8.dp),
                    text = user.login,
                )
            }
        }
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
private fun UserItemPreview(
    @PreviewParameter(UsersPreviewDataProvider::class) preview: ImmutableList<UserResource>,
) {
    AndroidLabTheme {
        UserItem(
            user = preview.first(),
        )
    }
}
