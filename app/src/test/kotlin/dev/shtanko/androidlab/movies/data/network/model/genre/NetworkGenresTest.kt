package dev.shtanko.androidlab.movies.data.network.model.genre

import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkGenresTest {
    private val jsonFormat = Json { ignoreUnknownKeys = true }

    @Test
    fun `serialize NetworkGenres model to JSON string`() {
        val serializedString: String =
            jsonFormat.encodeToString(NetworkGenres.serializer(), mockNetworkGenres)
        val expectedJsonString = """
            {
                "genres": [
                    {
                        "id": 28,
                        "name": "Action"
                    },
                    {
                        "id": 12,
                        "name": "Adventure"
                    }
                ]
            }
        """.trimIndent().replace("\n", "").replace(" ", "")

        assertEquals(expectedJsonString, serializedString.replace("\n", "").replace(" ", ""))
    }

    @Test
    fun `deserialize JSON string to NetworkGenres model`() {
        val jsonInput = """
            {
                "genres": [
                    {
                        "id": 28,
                        "name": "Action"
                    },
                    {
                        "id": 12,
                        "name": "Adventure"
                    }
                ]
            }
        """.trimIndent()
        val deserializedModel: NetworkGenres =
            jsonFormat.decodeFromString(NetworkGenres.serializer(), jsonInput)

        assertEquals(mockNetworkGenres, deserializedModel)
    }

    @Test
    fun mapToDomainModel() {
        val domainModel = mockNetworkGenres.toDomainModel()
        val expectedDomainModel = listOf(
            GenreEntity(28, "Action"),
            GenreEntity(12, "Adventure"),
        )

        assertEquals(expectedDomainModel, domainModel)
    }

    private val mockNetworkGenres = NetworkGenres(
        genres = listOf(
            NetworkGenre(28, "Action"),
            NetworkGenre(12, "Adventure"),
        ),
    )
}
