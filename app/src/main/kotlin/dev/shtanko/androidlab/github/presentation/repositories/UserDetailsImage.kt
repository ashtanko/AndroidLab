package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import dev.shtanko.androidlab.github.presentation.shared.thumbnailPlaceholderDefaultBrush

@Suppress("ModifierReused", "TrailingCommaOnDeclarationSite")
@Composable
fun UserDetailsImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderBrush: Brush = thumbnailPlaceholderDefaultBrush(),
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.background(MaterialTheme.colorScheme.primary))
        return
    }

    var imagePainterState by remember {
        mutableStateOf<AsyncImagePainter.State>(AsyncImagePainter.State.Empty)
    }

    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentScale = contentScale,
        onState = { state -> imagePainterState = state },
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when (imagePainterState) {
            is AsyncImagePainter.State.Loading,
            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = rememberVectorPainter(Icons.Default.Person),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                )
            }

            else -> {
                Box(
                    modifier = Modifier
                        .background(placeholderBrush)
                        .fillMaxSize(),

                    )
            }
        }

        Image(
            painter = imageLoader,
            contentDescription = contentDescription,
            contentScale = contentScale,
            modifier = modifier,
        )
    }
}
