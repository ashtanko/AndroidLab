package dev.shtanko.androidlab.movies.data.network.model.genre

import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkGenres(
    @SerialName("genres")
    val genres: List<NetworkGenre>,
)

fun NetworkGenre.toDomainModel() = GenreEntity(
    id = id,
    name = name,
)

fun NetworkGenres.toDomainModel() = genres.map { it.toDomainModel() }
