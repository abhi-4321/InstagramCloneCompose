package com.example.instagramclone.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarDestinations(
    val icon: ImageVector,
    val label: String,
    val screen: Screen,
) {
    HOME(
        label = "Home",
        icon = Icons.Filled.Home,
        screen = Screen.Home
    ),
    SEARCH(
        label = "Search",
        icon = Icons.Filled.Search,
        screen = Screen.Search
    ),
    CREATE(
        label = "Notifications",
        icon = Icons.TwoTone.AddCircle,
        screen = Screen.Create
    ),
    REELS(
        label = "Reels",
        icon = Icons.Filled.DateRange,
        screen = Screen.Reels
    ),
    PROFILE(
        label = "Profile",
        icon = Icons.Filled.AccountCircle,
        screen = Screen.Profile
    )
}