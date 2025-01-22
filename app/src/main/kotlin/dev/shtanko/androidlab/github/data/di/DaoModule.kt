package dev.shtanko.androidlab.github.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import dev.shtanko.androidlab.github.data.db.dao.UserDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesPhotosDao(
        database: GithubDatabase,
    ): UserDao = database.userDao()
}
