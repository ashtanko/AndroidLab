package dev.shtanko.androidlab.github.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shtanko.androidlab.github.presentation.model.UserResource

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "login") val login: String? = null,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null,
)

fun UserEntity.asExternalModel(): UserResource {
    return UserResource(
        id = id,
        login = login ?: "",
        avatarUrl = avatarUrl,
    )
}
