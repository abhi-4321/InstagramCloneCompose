package com.example.instagramclone.model

data class MessageGroup(
    val messages: List<Chat>,
    val shouldDisplayTime: Boolean
)
