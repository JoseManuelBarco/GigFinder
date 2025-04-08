package com.example.myapplication.api_objects

import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor:Interceptor {

     override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        SessionManager.token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}