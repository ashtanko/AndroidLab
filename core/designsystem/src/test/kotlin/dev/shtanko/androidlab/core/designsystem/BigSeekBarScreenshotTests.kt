package dev.shtanko.androidlab.core.designsystem

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.HiltTestApplication
import dev.shtanko.androidlab.core.designsystem.components.BigSeekBar
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
class BigSeekBarScreenshotTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun bigSeekBar_multipleThemes() {
        composeTestRule.captureMultiTheme("BigSeekBar") {
            BigSeekBarExample()
        }
    }

    @Test
    fun bigSeekBar_hugeFont() {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    DeviceConfigurationOverride.FontScale(2f),
                ) {
                    AndroidLabTheme {
                        BigSeekBarExample()
                    }
                }
            }
        }
        composeTestRule.onRoot()
            .captureRoboImage(
                "src/test/screenshots/BigSeekBar/BigSeekBar_fontScale2.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Composable
    private fun BigSeekBarExample() {
        var progress by remember { mutableFloatStateOf(0.5f) }
        BigSeekBar(
            progressProvider = { progress },
            onProgressChange = { progress = it },
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}
