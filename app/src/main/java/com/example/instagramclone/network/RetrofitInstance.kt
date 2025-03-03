package com.example.instagramclone.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.34:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: RetrofitInterface by lazy {
        retrofit.create(RetrofitInterface::class.java)
    }
}