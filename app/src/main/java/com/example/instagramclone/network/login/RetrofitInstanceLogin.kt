package com.example.instagramclone.network.login

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceLogin {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.33:3000")
//            .baseUrl("https://instagram-clone-3rjr.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: RetrofitInterfaceLogin by lazy {
        retrofit.create(RetrofitInterfaceLogin::class.java)
    }
}
