package com.example.instagramclone.network.login

import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.LoginResponse
import com.example.instagramclone.model.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitInterfaceLogin {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body loginRequest: LoginRequest) : Response<LoginResponse>
}