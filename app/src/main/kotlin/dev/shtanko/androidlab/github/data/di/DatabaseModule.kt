package dev.shtanko.androidlab.github.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.db.GithubDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideGithubDatabase(
        @ApplicationContext context: Context,
    ): GithubDatabase = Room
        .databaseBuilder(
            context,
            GithubDatabase::class.java,
            "gthb-db",
        )
        .fallbackToDestructiveMigration()
        .build()
}
