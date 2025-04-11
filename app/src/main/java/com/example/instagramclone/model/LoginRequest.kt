package com.example.instagramclone.model

data class LoginRequest(
    val email: String? = null,
    val username: String? = null,
    val password: String
)
