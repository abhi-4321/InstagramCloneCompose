package com.example.instagramclone.model

data class Comment(
    val id: Int,
    val userId: Int,
    val postId: Int,
    val comment: String,
    val likesCount: String,
    val likedBy: List<Int>
)