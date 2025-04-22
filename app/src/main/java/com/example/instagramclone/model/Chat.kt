package com.example.instagramclone.model

data class Chat(
    val id: String,
    val senderId: Int,
    val receiverId: Int,
    val chat: String,
    val attachment: String = "",
    val timestamp: String,
    val participants: List<Int>,
)
