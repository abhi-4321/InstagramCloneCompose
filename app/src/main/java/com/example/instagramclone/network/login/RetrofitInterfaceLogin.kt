package com.example.instagramclone.network.login

import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.LoginResponse
import com.example.instagramclone.model.OtpRequest
import com.example.instagramclone.model.OtpResponse
import com.example.instagramclone.model.RegistrationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterfaceLogin {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body registrationRequest: RegistrationRequest) : Response<LoginResponse>

    @POST("auth/sendOtp")
    suspend fun sendOtp(@Body otpRequest: OtpRequest) : Response<OtpResponse>

    @POST("auth/verifyOtp")
    suspend fun verifyOtp(@Body otpRequest: OtpRequest) : Response<OtpResponse>

    @GET
    suspend fun validateUsername(@Path("username") username: String) : Response<Void>
}