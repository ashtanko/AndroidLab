package dev.shtanko.lab.app.github.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.lab.app.github.data.repository.OfflineFirstRepositoriesRepository
import dev.shtanko.lab.app.github.data.repository.OfflineFirstUserRepository
import dev.shtanko.lab.app.github.data.repository.RepositoriesRepository
import dev.shtanko.lab.app.github.data.repository.UserRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    internal abstract fun bindsUserRepository(
        repository: OfflineFirstUserRepository,
    ): UserRepository

    @Binds
    internal abstract fun bindsRepositoriesRepository(
        repository: OfflineFirstRepositoriesRepository,
    ): RepositoriesRepository
}
