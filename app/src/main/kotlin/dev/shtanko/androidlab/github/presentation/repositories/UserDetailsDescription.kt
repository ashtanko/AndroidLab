package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews

@Composable
fun UserDetailsDescription(
    description: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    var isExpanded by remember { mutableStateOf(expanded) }
    var showSeeMore by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.clickable { isExpanded = !isExpanded },
    ) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = { result ->
                showSeeMore = result.hasVisualOverflow
            },
            modifier = Modifier.animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = EaseOutExpo,
                ),
            ),
        )
        if (showSeeMore) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.surface),
            ) {
                Text(
                    text = stringResource(id = R.string.see_more),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                    ),
                    modifier = Modifier.padding(start = 16.dp),
                )
            }
        }
    }
}

@ThemesPreviews
@Composable
private fun UserDetailsDescriptionExpandedPreview() {
    AndroidLabTheme {
        UserDetailsDescription(
            description = LoremIpsum(100).values.first(),
            expanded = true,
        )
    }
}

@ThemesPreviews
@Composable
private fun UserDetailsDescriptionPreview() {
    AndroidLabTheme {
        UserDetailsDescription(description = LoremIpsum(100).values.first())
    }
}
