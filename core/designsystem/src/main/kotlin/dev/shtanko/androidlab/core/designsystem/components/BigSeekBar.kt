package dev.shtanko.androidlab.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val DRAG_SENSITIVITY = 1.2f

@Composable
fun BigSeekBar(
    progressProvider: () -> Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.13f),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    var width by remember {
        mutableFloatStateOf(0f)
    }

    Canvas(
        modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .onPlaced {
                width = it.size.width.toFloat()
            }
            .pointerInput(progressProvider) {
                detectHorizontalDragGestures { _, dragAmount ->
                    onProgressChange(
                        (progressProvider() + dragAmount * DRAG_SENSITIVITY / width).coerceIn(
                            0f,
                            1f,
                        ),
                    )
                }
            },
    ) {
        drawRect(color = background)

        drawRect(
            color = color,
            size = size.copy(width = size.width * progressProvider()),
        )
    }
}

@Preview(name = "BigSeekBar - 0% progress", showBackground = true)
@Composable
private fun PreviewBigSeekBarZero() {
    var progress by remember { mutableFloatStateOf(0f) }
    BigSeekBar(
        progressProvider = { progress },
        onProgressChange = { progress = it },
        modifier = Modifier.padding(16.dp),
    )
}

@Preview(name = "BigSeekBar - 50% progress", showBackground = true)
@Composable
private fun PreviewBigSeekBarHalf() {
    var progress by remember { mutableFloatStateOf(0.5f) }
    BigSeekBar(
        progressProvider = { progress },
        onProgressChange = { progress = it },
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.secondary,
    )
}

@Preview(name = "BigSeekBar - Full progress", showBackground = true)
@Composable
private fun PreviewBigSeekBarFull() {
    var progress by remember { mutableFloatStateOf(1f) }
    BigSeekBar(
        progressProvider = { progress },
        onProgressChange = { progress = it },
        modifier = Modifier.padding(16.dp),
        color = Color.Green,
    )
}

@Preview(name = "BigSeekBar - Custom background", showBackground = true)
@Composable
private fun PreviewBigSeekBarCustomBackground() {
    var progress by remember { mutableFloatStateOf(0.3f) }
    BigSeekBar(
        progressProvider = { progress },
        onProgressChange = { progress = it },
        modifier = Modifier.padding(16.dp),
        background = Color.LightGray,
        color = Color.Magenta,
    )
}
