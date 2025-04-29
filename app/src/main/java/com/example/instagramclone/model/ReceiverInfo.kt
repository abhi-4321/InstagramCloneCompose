package com.example.instagramclone.model

import kotlinx.serialization.Serializable

@Serializable
data class ReceiverInfo(
    val receiverId: Int,
    val fullName: String,
    val profileImageUrl : String,
    val username: String
)
