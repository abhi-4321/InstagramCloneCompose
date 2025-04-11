package com.example.instagramclone.model

data class RegistrationRequest(
    val email: String,
    val username: String,
    val password: String,
    val fullName: String
)
