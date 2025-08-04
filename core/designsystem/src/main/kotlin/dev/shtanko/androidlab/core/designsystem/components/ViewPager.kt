package dev.shtanko.androidlab.core.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewPager(
    modifier: Modifier = Modifier,
    composableList: List<@Composable () -> Unit>,
    userScrollEnabled: Boolean = true,
) {
    HorizontalPager(
        state = rememberPagerState { composableList.size },
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .animateContentSize()
            .wrapContentHeight()
            .fillMaxWidth(),
        userScrollEnabled = userScrollEnabled,
    ) { page ->
        composableList[page]()
    }
}

@Composable
private fun SamplePage(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
    )
}

@Preview(showBackground = true)
@Composable
private fun ViewPagerPreview() {
    ViewPager(
        composableList = listOf(
            { SamplePage(Color.Red) },
            { SamplePage(Color.Green) },
            { SamplePage(Color.Blue) },
        ),
    )
}
