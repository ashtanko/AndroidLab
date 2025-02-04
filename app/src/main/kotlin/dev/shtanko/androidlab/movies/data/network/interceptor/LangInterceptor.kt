package dev.shtanko.androidlab.movies.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class LangInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        // Add the "lang" query parameter to the original URL
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("language", "en")
            .build()

        // Build a new request with the modified URL
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // Proceed with the new request
        return chain.proceed(newRequest)
    }
}
