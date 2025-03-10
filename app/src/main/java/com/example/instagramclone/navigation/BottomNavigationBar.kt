package com.example.instagramclone.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.instagramclone.ui.theme.WhiteVar

@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(navController: NavHostController, currentDestination: NavDestination?) {
    NavigationBar(
        containerColor = WhiteVar
    ) {
        BottomBarDestinations.entries.forEach { destination ->
            val selected = currentDestination?.hierarchy?.any {
                it.hasRoute(destination.screen::class)
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(destination.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}