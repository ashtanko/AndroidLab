@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun PullToRefresh(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = true,
    onRefresh: () -> Unit = {},
    testTag: String = "PullToRefreshBox",
    testIndicatorTag: String = "PullToRefreshIndicator",
    content: @Composable () -> Unit,
) {
    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        state = state,
        isRefreshing = isRefreshing,
        onRefresh = {
            onRefresh()
        },
        indicator = {
            Indicator(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .testTag(testIndicatorTag),
                isRefreshing = isRefreshing,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                state = state,
            )
        },
        modifier = modifier.testTag(testTag),
    ) {
        content()
    }
}
