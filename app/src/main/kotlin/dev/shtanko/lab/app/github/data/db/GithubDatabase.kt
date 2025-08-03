package dev.shtanko.lab.app.github.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.shtanko.lab.app.github.data.db.converter.DateConverter
import dev.shtanko.lab.app.github.data.db.dao.RepositoryDao
import dev.shtanko.lab.app.github.data.db.dao.RepositoryRemoteKeysDao
import dev.shtanko.lab.app.github.data.db.dao.UserDao
import dev.shtanko.lab.app.github.data.db.entity.RepositoryEntity
import dev.shtanko.lab.app.github.data.db.entity.RepositoryRemoteKeysEntity
import dev.shtanko.lab.app.github.data.db.entity.UserEntity
import dev.shtanko.lab.app.github.data.db.entity.UserEntityFull

@Database(
    entities = [
        UserEntity::class,
        RepositoryEntity::class,
        RepositoryRemoteKeysEntity::class,
        UserEntityFull::class,
    ],
    version = 3,
    autoMigrations = [],
    exportSchema = true,
)
@TypeConverters(DateConverter::class)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun repositoryRemoteKeysDao(): RepositoryRemoteKeysDao
    abstract fun repositoryDao(): RepositoryDao
}
