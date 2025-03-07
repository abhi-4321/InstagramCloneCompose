package com.example.instagramclone.network.main

import android.content.Context
import com.example.instagramclone.network.util.CustomAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstanceMain {
    private lateinit var apiService: RetrofitInterfaceMain

    fun getApiService(context: Context): RetrofitInterfaceMain {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.35:3000")
                .client(okhttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())// Add our Okhttp client
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

