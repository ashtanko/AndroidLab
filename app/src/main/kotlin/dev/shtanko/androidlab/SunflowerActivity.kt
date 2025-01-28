@file:Suppress("MagicNumber")
@file:OptIn(ExperimentalMaterial3Api::class)

package dev.shtanko.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.github.designsystem.ScreenBackground
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class SunflowerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidLabTheme {
                SunflowerApp()
            }
        }
    }
}

@Composable
fun SunflowerApp(
    modifier: Modifier = Modifier,
) {
    val primaryColor = Color(0xFFE91E63)

    ScreenBackground(
        modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
        ) { contentPadding ->
            Surface(modifier = Modifier.padding(contentPadding)) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    SunflowerScreen(
                        primaryColor = primaryColor,
                    )
                }
            }
        }
    }
}

@Composable
fun SunflowerScreen(
    primaryColor: Color,
    modifier: Modifier = Modifier,
) {
    var seeds by remember { mutableDoubleStateOf(100.0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(700.dp)
                .padding(16.dp),
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawSunflower(seeds.toInt(), primaryColor)
            }
        }
        Text(
            text = "Showing ${seeds.toInt()} seeds",
            style = MaterialTheme.typography.labelLarge,
        )

        Slider(
            value = seeds.toFloat(),
            onValueChange = { seeds = it.toDouble() },
            valueRange = 2.0f..15_000.0f,
            modifier = Modifier.width(300.dp),
        )
    }
}

fun DrawScope.drawSunflower(seeds: Int, color: Color) {
    val seedRadius = 2.0f
    val scaleFactor = 4.0f
    val tau = (PI * 2).toFloat()
    val phi = ((sqrt(5.0) + 1) / 2).toFloat()

    val center = size.width / 2

    for (i in 0 until seeds) {
        val theta = i * tau / phi
        val r = sqrt(i.toFloat()) * scaleFactor
        val x = center + r * cos(theta)
        val y = center - r * sin(theta)
        val offset = Offset(x, y)

        if (offset.x in 0f..size.width && offset.y in 0f..size.height) {
            drawCircle(color = color, radius = seedRadius, center = offset)
        }
    }
}
