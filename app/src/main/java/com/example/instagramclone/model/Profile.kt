package com.example.instagramclone.model

import androidx.navigation.NavHostController

data class Profile(
    val id: String,
    val username: String,
    val profileImageUrl: String,
    val postsCount: String,
    val followersCount: String,
    val followingCount: String,
    val fullName: String,
    val bio: String,
    val highlights: List<HighlightItem>,
    val posts: List<String>
) {
    constructor() : this("","","","","","","","", emptyList(), emptyList())
}
