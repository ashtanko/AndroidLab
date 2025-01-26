package dev.shtanko.androidlab.github.data

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.shtanko.androidlab.github.data.di.DataModule
import dev.shtanko.androidlab.github.data.repository.UserRepository
import dev.shtanko.androidlab.github.data.repository.user.FakeUserRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestDataModule {
    @Binds
    fun bindsTopicRepository(
        fakeUserRepository: FakeUserRepository,
    ): UserRepository
}
