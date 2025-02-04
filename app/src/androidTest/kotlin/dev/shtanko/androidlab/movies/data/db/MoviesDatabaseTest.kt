package dev.shtanko.androidlab.movies.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dev.shtanko.androidlab.movies.data.db.dao.GenreDao
import org.junit.After
import org.junit.Before

abstract class MoviesDatabaseTest {
    lateinit var db: MoviesDatabase
    lateinit var genreDao: GenreDao

    @Before
    fun setup() {
        db = run {
            val context = ApplicationProvider.getApplicationContext<Context>()
            Room.inMemoryDatabaseBuilder(
                context,
                MoviesDatabase::class.java,
            ).build()
        }
        genreDao = db.genreDao()
    }

    @After
    fun teardown() = db.close()
}
