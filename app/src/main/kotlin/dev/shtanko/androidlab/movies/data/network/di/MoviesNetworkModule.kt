package dev.shtanko.androidlab.movies.data.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.movies.data.network.interceptor.AuthInterceptor
import dev.shtanko.androidlab.movies.data.network.interceptor.LangInterceptor
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val JSON_CONTENT_TYPE = "application/json"
private const val CONNECT_TIMEOUT_SECONDS = 60L
private const val READ_TIMEOUT_SECONDS = 60L
private const val WRITE_TIMEOUT_SECONDS = 60L

@Module
@InstallIn(SingletonComponent::class)
internal object MoviesNetworkModule {
    @Provides
    @Singleton
    @Named("MoviesJson")
    fun providesMoviesNetworkJson(): Json = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("MoviesHttpLoggingInterceptor")
    fun provideMoviesHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    @Named("MoviesAuthInterceptor")
    fun provideMoviesAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    @Named("MoviesLangInterceptor")
    fun provideMoviesLangInterceptor(): LangInterceptor {
        return LangInterceptor()
    }

    @Provides
    @Singleton
    @Named("MoviesOkHttpClient")
    fun provideMoviesOkHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(httpLoggingInterceptor)
        .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @Named("MoviesRetrofit")
    fun provideMoviesRetrofit(
        networkJson: Json,
        okHttp: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttp)
        .addConverterFactory(networkJson.asConverterFactory(JSON_CONTENT_TYPE.toMediaType()))
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .build()
}
