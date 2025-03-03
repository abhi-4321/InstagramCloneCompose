package com.example.instagramclone.network

import com.example.instagramclone.model.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitInterface {

    @GET("/profile")
    suspend fun getUserList() : Response<List<Profile>>

    @GET("/profile/{id}")
    suspend fun getUser(@Path("id") id: String) : Response<Profile>

    @POST("/profile")
    suspend fun addUser(@Body user: Profile) : Response<Boolean>

    @PUT("/profile/{id}")
    suspend fun updateBio(@Path("id") id: String, @Body bio: String) : Response<Boolean>

    @DELETE("/profile/{id}")
    suspend fun deleteUser(@Path("id") id: String) : Response<Boolean>

}