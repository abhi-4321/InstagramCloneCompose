package com.example.instagramclone.network.login

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceLogin {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.34:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: RetrofitInterfaceLogin by lazy {
        retrofit.create(RetrofitInterfaceLogin::class.java)
    }
}
