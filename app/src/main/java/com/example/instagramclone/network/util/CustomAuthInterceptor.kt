package com.example.instagramclone.network.util

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CustomAuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer $token")
        return chain.proceed(requestBuilder.build())
    }
}