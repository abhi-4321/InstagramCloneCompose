package com.example.instagramclone.network.main

import android.content.Context
import com.example.instagramclone.network.util.CustomAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit


class RetrofitInstanceMain {
    private lateinit var apiService: RetrofitInterfaceMain

    fun getApiService(context: Context): RetrofitInterfaceMain {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
            .client(okhttpClient(context)) // Add our Okhttp client
                .build()

            apiService = retrofit.create(RetrofitInterfaceMain::class.java)
        }

        return apiService
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(CustomAuthInterceptor(context))
            .build()
    }
}

