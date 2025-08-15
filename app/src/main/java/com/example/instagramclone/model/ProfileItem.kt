package com.example.instagramclone.model

data class ProfileItem(
    val id: Int,
    val username: String,
    val profileImageUrl: String,
    val postsCount: String,
    val followersCount: String,
    val followingCount: String,
    val fullName: String,
    val bio: String,
    val highlights: List<HighlightItem>,
    val posts: List<PostDisplay>,
    val private: Boolean
) {
    constructor() : this(
        1,
        "_d_evil_02",
        "",
        "345",
        "654",
        "45",
        "Abhinav Mahalwal",
        "We forge the chains we wear in life",
        listOf(
            HighlightItem(
                id = 1,
                userId = 1,
                highlightUrl = "",
                title = "Mountains",
                highlighted = true
            ),
            HighlightItem(
                id = 2,
                userId = 1,
                highlightUrl = "",
                title = "Beaches",
                highlighted = true
            ),
            HighlightItem(
                id = 3,
                userId = 1,
                highlightUrl = "",
                title = "Oceans",
                highlighted = true
            ),
        ),
        listOf(
            PostDisplay(
                id = 1,
                userId = 1,
                postUrl = "",
                caption = "",
                likesCount = "12",
                likedBy = emptyList(),
                commentsCount = "",
                comments = emptyList(),
                createdAt = "2025-04-01T18:37:01.811Z",
                username = "HT",
                profileImageUrl = ""
            ), PostDisplay(
                id = 1,
                userId = 1,
                postUrl = "",
                caption = "",
                likesCount = "12",
                likedBy = emptyList(),
                commentsCount = "",
                comments = emptyList(),
                createdAt = "2025-04-01T18:37:01.811Z",
                username = "VC",
                profileImageUrl = ""
            ), PostDisplay(
                id = 1,
                userId = 1,
                postUrl = "",
                caption = "",
                likesCount = "12",
                likedBy = emptyList(),
                commentsCount = "",
                comments = emptyList(),
                createdAt = "2025-04-01T18:37:01.811Z",
                username = "AB",
                profileImageUrl = ""
            )
        ),
        false
    )
}
