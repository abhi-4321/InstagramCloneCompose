package com.example.instagramclone.model

data class SearchResponse(
    val users: List<SearchUser>,
    val page: Int,
    val totalPages: Int,
    val totalResults: Int
)

