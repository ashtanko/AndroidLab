@file:Suppress("ImportOrdering")

package dev.shtanko.androidlab.github.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.shtanko.androidlab.github.data.service.UserService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val BASE_URL = "https://api.github.com/"
private const val JSON_CONTENT_TYPE = "application/json"
private const val CONNECT_TIMEOUT_SECONDS = 60L
private const val READ_TIMEOUT_SECONDS = 60L
private const val WRITE_TIMEOUT_SECONDS = 60L

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {
    @Provides
    @Singleton
    fun providesNetworkJson() = Json {
        coerceInputValues = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    @Named("HttpLoggingInterceptor")
    fun provideHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(
        httpLoggingInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        networkJson: Json,
        okHttp: OkHttpClient,
    ): Retrofit {
        val contentType = JSON_CONTENT_TYPE.toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(
        networkJson: Json,
        okHttp: OkHttpClient,
        baseUrl: String = BASE_URL,
        defaultRetrofit: Retrofit,
    ): UserService {
        return createService(
            networkJson = networkJson,
            okHttp = okHttp,
            baseUrl = baseUrl,
            retrofit = null,
        )
    }
}

private inline fun <reified T> createService(
    networkJson: Json,
    okHttp: OkHttpClient,
    retrofit: Retrofit?,
    baseUrl: String = BASE_URL,
): T {
    val contentType = JSON_CONTENT_TYPE.toMediaType()
    return if (retrofit != null) {
        retrofit.create(T::class.java)
    } else {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp)
            .addConverterFactory(networkJson.asConverterFactory(contentType))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
            .create(T::class.java)
    }
}
