package dev.shtanko.lab.app.github.presentation.repository

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.paging.PagingData
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.lab.app.github.presentation.repositories.RepositoriesUiState
import dev.shtanko.androidlab.core.screenshottesting.captureMultiTheme
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
@Config(application = HiltTestApplication::class, qualifiers = "480dpi", sdk = [35])
@LooperMode(LooperMode.Mode.PAUSED)
class RepositoriesScreenThemesScreenshotTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val preview = RepositoriesDataProvider().values.first()

    @Test
    fun repositoriesScreenPopulatedItemsMultipleThemes() {
        composeTestRule.captureMultiTheme("RepositoryScreenPopulatedItemsThemes") { description ->
            AndroidLabTheme {
                val pagingData = PagingData.from(preview.second)
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
}
