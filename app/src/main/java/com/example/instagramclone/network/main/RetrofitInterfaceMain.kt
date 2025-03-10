package com.example.instagramclone.network.main

import com.example.instagramclone.model.ProfileItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface RetrofitInterfaceMain {

    @GET("/user")
    suspend fun getUser() : Response<ProfileItem>

    @PUT("/profile/{id}")
    suspend fun updateBio(@Path("id") id: String, @Body bio: String) : Response<Boolean>

    @DELETE("/profile/{id}")
    suspend fun deleteUser(@Path("id") id: String) : Response<Boolean>

}