package dev.shtanko.androidlab.github.presentation.repositories

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.shtanko.androidlab.github.presentation.model.RepositoryResource
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import dev.shtanko.androidlab.github.presentation.preview.RepositoriesDataProvider
import dev.shtanko.androidlab.github.presentation.shared.CircularAvatarImage
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemesPreviews
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RepositoryItem(
    item: RepositoryResource,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
) {
    Box(modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainer,
            onClick = {
                onClick(item.id)
            },
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularAvatarImage(
                        sizeDp = 16.dp,
                        borderDp = 0.dp,
                        imageUrl = item.owner?.avatarUrl,
                    )
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        text = item.owner?.login.orEmpty(),
                    )
                }
                Row(
                    modifier = Modifier.padding(bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier,
                        text = item.name.orEmpty(),
                    )
                }
                item.description.takeIf { it?.isNotEmpty() == true }?.let { description ->
                    Text(
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = description,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Rounded.Star,
                            contentDescription = null,
                        )
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier,
                            text = item.stars.toString(),
                        )
                    }
                    Text(
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp),
                        text = item.language.orEmpty(),
                    )
                }
            }
        }
    }
}

@ThemesPreviews
@Composable
private fun RepositoryItemPreview(
    @PreviewParameter(RepositoriesDataProvider::class) preview: Pair<UserFullResource, ImmutableList<RepositoryResource>>,
) {
    AndroidLabTheme {
        RepositoryItem(item = preview.second.first())
    }
}
