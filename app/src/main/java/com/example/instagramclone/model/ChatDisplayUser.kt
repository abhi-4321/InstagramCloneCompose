package com.example.instagramclone.model

data class ChatDisplayUser(
    val receiverId: Int,
    val fullName: String,
    val profileImageUrl : String,
    val lastChat: Chat
)
