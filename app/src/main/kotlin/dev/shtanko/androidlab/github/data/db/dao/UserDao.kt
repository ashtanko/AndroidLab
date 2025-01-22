package dev.shtanko.androidlab.github.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shtanko.androidlab.github.data.db.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getUsers(): List<UserEntity>?

    @Query("DELETE FROM users")
    suspend fun clearAllUsers()
}
