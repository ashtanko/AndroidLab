package dev.shtanko.androidlab.github.presentation.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.forwardingPainter

@Composable
fun CircularAvatarImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 64.dp,
    borderDp: Dp = 1.dp,
    placeholderIcon: ImageVector = Icons.Default.Person,
    errorIcon: ImageVector = Icons.Default.Person,
) {
    Box(
        modifier = modifier
            .size(sizeDp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .border(borderDp, MaterialTheme.colorScheme.primary, CircleShape),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = forwardingPainter(
                painter = rememberVectorPainter(placeholderIcon),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = rememberVectorPainter(errorIcon),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircularAvatarImagePreview() {
    AndroidLabTheme {
        CircularAvatarImage(
            sizeDp = 32.dp,
            imageUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
        )
    }
}
