package dev.shtanko.androidlab.github.presentation.repository

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.util.captureMultiTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class RepositoriesScreenThemesScreenshotTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val repoResources = RepositoriesDataProvider().values.first()

    @Test
    fun repositoriesScreenPopulatedItemsMultipleThemes() {
        composeTestRule.captureMultiTheme("RepositoryScreenPopulatedItemsThemes") { description ->
            AndroidLabTheme {
                val pagingData = PagingData.from(repoResources)
                val flow = MutableStateFlow(pagingData)
                RepositoriesScreen(
                    uiState = flow.collectAsLazyPagingItems(),
                    isRefreshing = false,
                )
            }
        }
    }
}
