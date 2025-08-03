package dev.shtanko.androidlab.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.shtanko.androidlab.core.designsystem.theme.AndroidLabTheme

/**
 * Auto-resizing Text composable.
 * Source adapted from: https://stackoverflow.com/a/69780826
 */
@Composable
fun AutoResizeText(
    text: String,
    fontSizeRange: FontSizeRange,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = LocalTextStyle.current,
) {
    var fontSizeValue by remember(text) {
        mutableFloatStateOf(fontSizeRange.max.value)
    }
    var readyToDraw by remember(text) { mutableStateOf(false) }

    // Ensure redrawing is triggered when text changes
    LaunchedEffect(text) {
        fontSizeValue = fontSizeRange.max.value
        readyToDraw = false
    }

    Text(
        text = text,
        modifier = modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        color = color,
        fontSize = fontSizeValue.sp,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        style = style,
        onTextLayout = { layoutResult ->
            if (!readyToDraw) {
                if (layoutResult.didOverflowHeight) {
                    val nextFontSize = fontSizeValue - fontSizeRange.step.value
                    if (nextFontSize <= fontSizeRange.min.value) {
                        fontSizeValue = fontSizeRange.min.value
                        readyToDraw = true
                    } else {
                        fontSizeValue = nextFontSize
                    }
                } else {
                    readyToDraw = true
                }
            }
        },
    )
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    init {
        require(min < max) { "min should be less than max, $this" }
        require(step.value > 0) { "step should be greater than 0, $this" }
    }

    companion object {
        private val DEFAULT_TEXT_STEP = 1.sp
    }
}

@Preview(name = "Short Text")
@Composable
fun PreviewAutoResizeTextShort() {
    AndroidLabTheme {
        AutoResizeText(
            text = "Short text",
            fontSizeRange = FontSizeRange(min = 12.sp, max = 24.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
}

@Preview(name = "Long Text - Fixed", showBackground = false)
@Composable
fun PreviewAutoResizeTextLong() {
    AndroidLabTheme {
        AutoResizeText(
            text = "This is a very long text that should trigger the auto resize behavior by " +
                "overflowing the available height and requiring smaller font sizes to fit " +
                "properly.",
            maxLines = 2,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            fontSizeRange = FontSizeRange(16.sp, 22.sp),
            fontStyle = FontStyle.Italic,
            color = Color.Blue,
        )
    }
}

@Preview(name = "Exact Fit Text")
@Composable
fun PreviewAutoResizeTextExactFit() {
    AndroidLabTheme {
        AutoResizeText(
            text = "Exact fit text",
            fontSizeRange = FontSizeRange(min = 16.sp, max = 17.sp), // Fixed font size
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.Yellow,
        )
    }
}

@Preview(name = "Multiple Lines Text")
@Composable
fun PreviewAutoResizeTextMultiline() {
    AndroidLabTheme {
        AutoResizeText(
            text = "This is an example of multiline text. It should wrap properly and shrink the " +
                "font size to fit within the max height if necessary.",
            fontSizeRange = FontSizeRange(min = 10.sp, max = 24.sp),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Magenta,
        )
    }
}
