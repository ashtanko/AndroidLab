package dev.shtanko.androidlab.movies.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shtanko.androidlab.movies.data.db.entity.genre.GenreEntity

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Query("SELECT * FROM genres")
    fun getGenres(): List<GenreEntity>?

    @Query("DELETE FROM genres")
    suspend fun clearAllGenres()
}
