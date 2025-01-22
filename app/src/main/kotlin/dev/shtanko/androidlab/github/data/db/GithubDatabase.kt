package dev.shtanko.androidlab.github.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shtanko.androidlab.github.data.db.dao.UserDao
import dev.shtanko.androidlab.github.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
