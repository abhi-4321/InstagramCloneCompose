package com.example.instagramclone.model

data class UserDetailsUpdateRequest(
    val fullName: String?,
    val username: String,
    val bio: String?
)
