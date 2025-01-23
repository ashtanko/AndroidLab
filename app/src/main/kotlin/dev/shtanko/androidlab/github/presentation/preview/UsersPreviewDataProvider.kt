package dev.shtanko.androidlab.github.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import dev.shtanko.androidlab.github.presentation.model.UserResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class UsersPreviewDataProvider : PreviewParameterProvider<ImmutableList<UserResource>> {
    override val values: Sequence<ImmutableList<UserResource>>
        get() = sequenceOf(
            persistentListOf(
                UserResource(
                    id = 1,
                    login = LoremIpsum(10).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                ),
                UserResource(
                    id = 2,
                    login = LoremIpsum(5).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                ),
                UserResource(
                    id = 3,
                    login = LoremIpsum(50).values.first(),
                    avatarUrl = "https://avatars.githubusercontent.com/u/32689599?v=4",
                ),
            ),
        )
}
