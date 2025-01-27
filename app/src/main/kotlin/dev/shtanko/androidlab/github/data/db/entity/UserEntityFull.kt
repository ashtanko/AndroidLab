package dev.shtanko.androidlab.github.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.shtanko.androidlab.github.presentation.model.UserFullResource
import java.util.Date

@Entity(tableName = "full_users")
data class UserEntityFull(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "login") val login: String? = null,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String? = null,
    @ColumnInfo("name") val name: String? = null,
    @ColumnInfo("company") val company: String? = null,
    @ColumnInfo("blog") val blog: String? = null,
    @ColumnInfo("location") val location: String? = null,
    @ColumnInfo("hireable") val hireable: Boolean? = null,
    @ColumnInfo("bio") val bio: String? = null,
    @ColumnInfo("twitter_username") val twitter: String? = null,
    @ColumnInfo("public_repos") val publicReposCount: Int? = null,
    @ColumnInfo("public_gists") val publicGistsCount: Int? = null,
    @ColumnInfo("followers") val followers: Int? = null,
    @ColumnInfo("following") val following: Int? = null,
    @ColumnInfo("created_at") val createdAt: Date? = null,
)

fun UserEntityFull.asExternalModel(): UserFullResource {
    return UserFullResource(
        id = id,
        login = login,
        avatarUrl = avatarUrl,
        name = name,
        company = company,
        blog = blog,
        location = location,
        hireable = hireable,
        bio = bio,
        twitter = twitter,
        publicReposCount = publicReposCount,
        publicGistsCount = publicGistsCount,
        followers = followers,
        following = following,
        createdAt = createdAt,
    )
}
