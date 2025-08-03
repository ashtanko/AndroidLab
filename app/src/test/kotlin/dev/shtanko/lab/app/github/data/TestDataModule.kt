package dev.shtanko.lab.app.github.data

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.shtanko.lab.app.github.data.di.DataModule
import dev.shtanko.lab.app.github.data.repository.UserRepository
import dev.shtanko.lab.app.github.data.repository.user.FakeUserRepository

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
