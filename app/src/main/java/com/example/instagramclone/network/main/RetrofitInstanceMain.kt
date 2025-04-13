package com.example.instagramclone.network.main

import android.content.Context
import com.example.instagramclone.network.util.CustomAuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstanceMain {
    fun getApiService(token: String): RetrofitInterfaceMain {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.33:3000")
//            .baseUrl("https://instagram-clone-3rjr.onrender.com")
            .client(okhttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())// Add our Okhttp client
            .build()

        return retrofit.create(RetrofitInterfaceMain::class.java)
    }

    private fun okhttpClient(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(CustomAuthInterceptor(token))
            .build()
    }
}

