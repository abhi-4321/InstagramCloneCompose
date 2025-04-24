package com.example.instagramclone.network.main

import com.example.instagramclone.model.AllChatUsers
import com.example.instagramclone.model.Chat
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.StoryDisplayUser
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

    @GET("/post")
    suspend fun fetchFeed() : Response<List<PostDisplay>>

    @GET("/story")
    suspend fun fetchDisplayUsers() : Response<List<StoryDisplayUser>>

    @GET("/chat/users")
    suspend fun fetchChatUsers() : Response<List<ChatDisplayUser>>

    @GET("/chat/all")
    suspend fun fetchAllChatUsers() : Response<List<AllChatUsers>>

    @GET("/chat/{receiverId}")
    suspend fun fetchPreviousChats() : Response<List<Chat>>
}