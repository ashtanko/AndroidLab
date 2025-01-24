package dev.shtanko.androidlab.github.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.shtanko.androidlab.github.data.db.dao.RepositoryDao
import dev.shtanko.androidlab.github.data.db.dao.RepositoryRemoteKeysDao
import dev.shtanko.androidlab.github.data.db.dao.UserDao
import dev.shtanko.androidlab.github.data.db.entity.RepositoryEntity
import dev.shtanko.androidlab.github.data.db.entity.RepositoryRemoteKeysEntity
import dev.shtanko.androidlab.github.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RepositoryEntity::class,
        RepositoryRemoteKeysEntity::class,
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true,
)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryRemoteKeysDao(): RepositoryRemoteKeysDao
    abstract fun repositoryDao(): RepositoryDao
}
