package dev.shtanko.lab.app.github.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.shtanko.lab.app.github.data.db.entity.UserEntity
import dev.shtanko.lab.app.github.data.db.entity.UserEntityFull

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    fun getUsers(): List<UserEntity>?

    @Query("DELETE FROM users")
    suspend fun clearAllUsers()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFullUser(entity: UserEntityFull)

    @Query(value = "SELECT * FROM full_users ORDER BY id DESC LIMIT 1")
    fun getFullUser(): UserEntityFull?

    @Query("SELECT * FROM full_users WHERE login = :login")
    fun getFullUserByLogin(login: String): UserEntityFull?

    @Query("DELETE FROM full_users")
    fun clearAllFullUsers()
}
