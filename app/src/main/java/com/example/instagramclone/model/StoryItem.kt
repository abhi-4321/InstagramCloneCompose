package com.example.instagramclone.model

data class StoryItem(
    val id: Int,
    val userId: Int,
    val storyUrl: String,
    val likedBy: List<Int>,
    val createdAt: String
)
