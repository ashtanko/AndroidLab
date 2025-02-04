package dev.shtanko.androidlab.movies.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shtanko.androidlab.movies.data.db.dao.GenreDao
import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity

@Database(
    entities = [
        GenreEntity::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
}
