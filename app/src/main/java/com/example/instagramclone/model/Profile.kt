package com.example.instagramclone.model

data class Profile(
    val id: Int,
    val username: String,
    val profileImageUrl: String,
    val postsCount: String,
    val followersCount: String,
    val followingCount: String,
    val fullName: String,
    val bio: String,
    val highlights: List<HighlightItem>,
    val posts: List<Post>,
    val private: Boolean
) {
    constructor() : this(1,"","","","","","","", emptyList(), emptyList(),false)
}
