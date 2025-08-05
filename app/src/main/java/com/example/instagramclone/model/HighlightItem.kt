package com.example.instagramclone.model

data class HighlightItem(
    val id: Int,
    val userId: Int,
    val highlightUrl: String,
    val highlighted: Boolean,
    val title: String
)
