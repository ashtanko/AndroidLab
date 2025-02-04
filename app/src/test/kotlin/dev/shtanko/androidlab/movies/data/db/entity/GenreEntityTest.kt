package dev.shtanko.androidlab.movies.data.db.entity

import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GenreEntityTest {
    @Test
    fun `verify default values in GenresEntity`() {
        val genreEntity = GenreEntity(id = 1, name = "Genre1")

        assertEquals(1, genreEntity.id)
        assertEquals("Genre1", genreEntity.name)
    }
}
