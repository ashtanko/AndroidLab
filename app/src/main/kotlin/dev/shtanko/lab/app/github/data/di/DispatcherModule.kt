package dev.shtanko.lab.app.github.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.lab.app.utils.executorDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DispatcherModule {
    @DefaultDispatcher
    @Singleton
    @Provides
    fun defaultDispatcher(): CoroutineDispatcher = executorDispatcher {
        Executors.newCachedThreadPool()
    }

    @DecodingDispatcher
    @Singleton
    @Provides
    fun decodingDispatcher(): CoroutineDispatcher = executorDispatcher {
        Executors.newFixedThreadPool(
            2 * Runtime.getRuntime().availableProcessors() + 1,
        )
    }

    @EncodingDispatcher
    @Singleton
    @Provides
    fun encodingDispatcher(): CoroutineDispatcher = executorDispatcher {
        Executors.newSingleThreadExecutor()
    }

    @IoDispatcher
    @Singleton
    @Provides
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @UiDispatcher
    @Singleton
    @Provides
    fun uiDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
