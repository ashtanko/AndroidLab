package dev.shtanko.androidlab.movies.data.network.interceptor

import dev.shtanko.androidlab.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()
        val reqBuilder =
            originalReq.newBuilder().header("Authorization: Bearer ", BuildConfig.TMDB_API_KEY)
        val request = reqBuilder.build()

        return chain.proceed(request)
    }
}
