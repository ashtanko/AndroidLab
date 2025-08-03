package dev.shtanko.androidlab.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.core.designsystem.components.EmptyPlaceholder
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme
import dev.shtanko.androidlab.core.screenshottesting.DefaultRoborazziOptions
import dev.shtanko.androidlab.core.screenshottesting.captureMultiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@OptIn(ExperimentalMaterial3Api::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class EmptyPlaceholderScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun emptyPlaceholder_multipleThemes() {
        composeTestRule.captureMultiTheme("EmptyPlaceholder") {
            EmptyPlaceholderExample()
        }
    }

    @Test
    fun emptyPlaceholder_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.FontScale(2f),
                ) {
                    AndroidLabTheme {
                        EmptyPlaceholderExample()
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/EmptyPlaceholder/EmptyPlaceholder_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Composable
    private fun EmptyPlaceholderExample() {
        EmptyPlaceholder(
            icon = android.R.drawable.ic_dialog_alert,
            text = "No internet connection",
        )
    }
}
