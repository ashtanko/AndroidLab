package dev.shtanko.androidlab.movies.data.dao

import androidx.test.ext.junit.runners.AndroidJUnit4
import dev.shtanko.androidlab.movies.data.db.MoviesDatabaseTest
import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GenreDaoTest : MoviesDatabaseTest() {
    @Test
    fun insertUsers_insertsUsersSuccessfully() = runTest {
        genreDao.insertAll(genres = mockGenres)
        val genresFromDb = genreDao.getGenres()
        assertNotNull(genresFromDb)
        assertEquals(2, genresFromDb?.size)
        assertEquals(mockGenres, genresFromDb)
    }

    val mockGenres = listOf(
        GenreEntity(
            id = 1,
            name = "Genre1",
        ),
        GenreEntity(
            id = 2,
            name = "Genre2",
        ),
    )
}
