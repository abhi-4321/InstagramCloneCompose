package com.example.instagramclone.navigation

import android.net.Uri
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Birthday : Screen

    @Serializable
    data object Confirmation : Screen

    @Serializable
    data object EnterPassword : Screen

    @Serializable
    data object ForgotPassword : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object Name : Screen

    @Serializable
    data class ProfilePicture(val token: String) : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data object SaveInfo : Screen

    @Serializable
    data object TermsAndPolicies : Screen

    @Serializable
    data object Username : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data object Create : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Reels : Screen

    @Serializable
    data object Search : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Messages : Screen

    @Serializable
    data object NewMessage : Screen

    @Serializable
    data class Chat(val receiverId: Int, val profileImageUrl: String, val username: String, val fullName: String, val senderId: Int) : Screen

    @Serializable
    data class EditPost(val uri: String) : Screen

    @Serializable
    data class Story(val userId: Int, val profileImageUrl: String, val username: String, val fullName: String) : Screen

    @Serializable
    data object AddHighLight : Screen

    @Serializable
    data class HighlightTitle(val url: String) : Screen

    @Serializable
    data class ViewPost(val postId: Int, val from: String) : Screen
}