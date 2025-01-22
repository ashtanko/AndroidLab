package dev.shtanko.androidlab.github.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.repository.OfflineFirstUserRepository
import dev.shtanko.androidlab.github.data.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsUserRepository(
        repository: OfflineFirstUserRepository,
    ): UserRepository
}
