package dev.shtanko.androidlab.github.designsystem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme

@Composable
fun ScreenBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            )
            content()
        }
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
private fun ScreenBackgroundPreview() {
    AndroidLabTheme {
        ScreenBackground {
            Text("Background")
        }
    }
}
