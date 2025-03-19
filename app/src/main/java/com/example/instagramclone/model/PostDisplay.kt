package com.example.instagramclone.model

data class PostDisplay(
    val id: Int,
    val userId: Int,
    val postUrl: String,
    val caption: String,
    val likesCount: String,
    val likedBy: List<Int>,
    val commentsCount: String,
    val comments: List<Comment>,
    val username: String,
    val profileImageUrl: String
)
