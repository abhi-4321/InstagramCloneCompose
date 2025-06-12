package com.example.instagramclone.network.main

import com.example.instagramclone.model.Chat
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.model.StoryItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

interface RetrofitInterfaceMain {

    @GET("/user")
    suspend fun getUser(): Response<ProfileItem>

    @PUT("/profile/{id}")
    suspend fun updateBio(@Path("id") id: String, @Body bio: String): Response<Boolean>

    @DELETE("/profile/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Boolean>

    @GET("/post")
    suspend fun fetchFeed(): Response<List<PostDisplay>>

    @GET("/story")
    suspend fun fetchDisplayUsers(): Response<List<StoryDisplayUser>>

    @GET("/chat/users")
    suspend fun fetchChatUsers(): Response<List<ChatDisplayUser>>

    @GET("/chat/all")
    suspend fun fetchAllChatUsers(): Response<List<ChatDisplayUser>>

    @GET("/chat/{receiverId}")
    suspend fun fetchPreviousChats(@Path("receiverId") receiverId: Int): Response<List<Chat>>

    @Multipart
    @POST("/post")
    suspend fun createPost(@Part("caption") caption: RequestBody, @Part image: MultipartBody.Part): Response<Void>

    @GET("/story/{userId}")
    suspend fun getStories(@Path("userId") userId: Int) : Response<List<StoryItem>>
}