package com.example.instagramclone.navigation

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
    data object ProfilePicture : Screen

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
}