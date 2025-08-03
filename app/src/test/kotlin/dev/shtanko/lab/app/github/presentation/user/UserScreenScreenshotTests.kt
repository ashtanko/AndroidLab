package dev.shtanko.lab.app.github.presentation.user

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.lab.app.github.presentation.preview.UsersPreviewDataProvider
import dev.shtanko.lab.app.github.presentation.users.UserUiState
import dev.shtanko.lab.app.github.presentation.users.UsersContent
import dev.shtanko.androidlab.core.screenshottesting.captureMultiDevice
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [35])
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
