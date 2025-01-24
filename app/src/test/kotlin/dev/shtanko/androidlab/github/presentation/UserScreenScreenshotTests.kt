package dev.shtanko.androidlab.github.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.github.presentation.preview.UsersPreviewDataProvider
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.util.captureMultiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class)
@LooperMode(LooperMode.Mode.PAUSED)
class UserScreenScreenshotTests {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val usersResources = UsersPreviewDataProvider().values.first()

    @Test
    fun usersScreenPopulatedItems() {
        composeTestRule.captureMultiDevice("UsersScreenPopulatedItems") {
            AndroidLabTheme {
                UsersContent(
                    uiState = UserUiState.Success(
                        users = usersResources,
                    ),
                    isRefreshing = false,
                )
            }
        }
    }

    @Test
    fun usersScreenPopulatedItemsRefreshingState() {
        composeTestRule.captureMultiDevice("UsersScreenPopulatedItemsRefreshingState") {
            AndroidLabTheme {
                UsersContent(
                    uiState = UserUiState.Success(
                        users = usersResources,
                    ),
                    isRefreshing = true,
                )
            }
        }
    }

    @Test
    fun usersScreenError() {
        composeTestRule.captureMultiDevice("UsersScreenError") {
            AndroidLabTheme {
                UsersContent(
                    uiState = UserUiState.Error,
                    isRefreshing = false,
                )
            }
        }
    }

    @Test
    fun usersScreenEmpty() {
        composeTestRule.captureMultiDevice("UsersScreenEmpty") {
            AndroidLabTheme {
                UsersContent(
                    uiState = UserUiState.Empty,
                    isRefreshing = false,
                )
            }
        }
    }

    @Test
    fun usersScreenLoading() {
        composeTestRule.captureMultiDevice("UsersScreenLoading") {
            AndroidLabTheme {
                UsersContent(
                    uiState = UserUiState.Loading,
                    isRefreshing = false,
                )
            }
        }
    }
}
