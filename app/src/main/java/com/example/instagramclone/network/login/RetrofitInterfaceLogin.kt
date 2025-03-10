package com.example.instagramclone.network.login

import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitInterfaceLogin {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}