package dev.shtanko.androidlab.github.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.db.dao.RepositoryDao
import dev.shtanko.androidlab.github.data.db.dao.RepositoryRemoteKeysDao
import dev.shtanko.androidlab.github.data.db.dao.UserDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesUserDao(
        database: GithubDatabase,
    ): UserDao = database.userDao()

    @Provides
    fun providesRepositoryDao(
        database: GithubDatabase,
    ): RepositoryDao = database.repositoryDao()

    @Provides
    fun providesRepositoryRemoteKeysDao(
        database: GithubDatabase,
    ): RepositoryRemoteKeysDao = database.repositoryRemoteKeysDao()
}
