package com.example.instagramclone.network.main

import com.example.instagramclone.model.ChangePasswordRequest
import com.example.instagramclone.model.Chat
import com.example.instagramclone.model.ChatDisplayUser
import com.example.instagramclone.model.CommentRequest
import com.example.instagramclone.model.FollowUserItem
import com.example.instagramclone.model.HighlightItem
import com.example.instagramclone.model.LikeResponse
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.SearchResponse
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.model.UpdateCaptionRequest
import com.example.instagramclone.model.UserDetailsUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterfaceMain {

    // Post Routes

    @GET("/post")
    suspend fun fetchFeed(): Response<List<PostDisplay>>

    @GET("/post/all")
    suspend fun getAllPosts(): Response<List<PostDisplay>>

    @GET("/post/explore")
    suspend fun explorePosts(): Response<List<PostDisplay>>

    @GET("/post/saved")
    suspend fun fetchSavedPosts(): Response<List<PostDisplay>>

    @POST("/post/save/{postId}")
    suspend fun savePost(@Path("postId") postId: Int): Response<Void>

    @DELETE("/post/saved/{postId}")
    suspend fun unSavePost(@Path("postId") postId: Int): Response<Void>

    @POST("/post/likeComment/{commentId}")
    suspend fun likeComment(@Path("commentId") commentId: Int): Response<Void>

    @POST("/post/comment/{postId}")
    suspend fun commentOnPost(@Path("postId") postId: Int, @Body comment: CommentRequest): Response<Void>

    @DELETE("/post/{postId}")
    suspend fun deletePost(@Path("postId") postId: Int): Response<Void>

    @GET("/post/{postId}")
    suspend fun getPostById(@Path("postId") postId: Int): Response<PostDisplay>

    @PUT("/post/{postId}")
    suspend fun updatePostCaption(@Path("postId") postId: Int, @Body caption: UpdateCaptionRequest): Response<Void>

    @Multipart
    @POST("/post")
    suspend fun createPost(
        @Part("caption") caption: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Void>

    @POST("/post/like/{postId}")
    suspend fun like(@Path("postId") postId: Int): Response<LikeResponse>

    // Highlight Routes

    @GET("/highlight")
    suspend fun getAllHighlights(): Response<List<HighlightItem>>

    @GET("/highlight/stories")
    suspend fun getPastStories(): Response<List<StoryItem>>

    @POST("/highlight/{highlightId}")
    suspend fun createHighlight(@Path("highlightId") highlightId: Int): Response<Void>

    @DELETE("/highlight/{highlightId}")
    suspend fun deleteHighlight(@Path("highlightId") highlightId: Int): Response<Void>

    @GET("/highlight/{highlightId}")
    suspend fun getHighlight(@Path("highlightId") highlightId: Int): Response<HighlightItem>

    // Stories Routes

    @GET("/story")
    suspend fun fetchDisplayUsers(): Response<List<StoryDisplayUser>>

    @POST("/story/like/{storyId}")
    suspend fun likeStory(@Path("storyId") storyId: Int): Response<Void>

    @Multipart
    @POST("/story")
    suspend fun createStory(
        @Part("caption") caption: RequestBody,
        @Part image: MultipartBody.Part
    ): Response<Void>

    @DELETE("/story/{storyId}")
    suspend fun deleteStory(@Path("storyId") storyId: Int): Response<Void>

    @GET("/story/{userId}")
    suspend fun getStories(@Path("userId") userId: Int): Response<List<StoryItem>>

    // User Routes

    @PUT("/user/changePassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<Void>

    @PATCH("/user/changeVisibility")
    suspend fun changeVisibility(): Response<Void>

    @Multipart
    @POST("/user/profileImage")
    suspend fun uploadProfileImage(@Part image: MultipartBody.Part): Response<Void>

    @PUT("/user/details")
    suspend fun updateUserDetails(@Body details: UserDetailsUpdateRequest): Response<Void>

    @GET("/user/list")
    suspend fun getAllUsers(): Response<List<ProfileItem>>

    @GET("/user/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): Response<ProfileItem>

    @GET("/user")
    suspend fun getUser(): Response<ProfileItem>

    @GET("search")
    suspend fun searchUsers(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): SearchResponse

    // Follow

    @GET("/user/followersList")
    suspend fun getFollowers(): Response<FollowUserItem>

    @GET("/user/followingList")
    suspend fun getFollowing(): Response<FollowUserItem>

    @POST("/user/follow/{followedTo}")
    suspend fun followUser(@Path("followedTo") userId: Int): Response<Void>

    // Chat

    @GET("/chat/users")
    suspend fun fetchChatUsers(): Response<List<ChatDisplayUser>>

    @GET("/chat/all")
    suspend fun fetchAllChatUsers(): Response<List<ChatDisplayUser>>

    @GET("/chat/{receiverId}")
    suspend fun fetchPreviousChats(@Path("receiverId") receiverId: Int): Response<List<Chat>>

}