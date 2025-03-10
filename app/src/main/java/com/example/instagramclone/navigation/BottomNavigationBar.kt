package com.example.instagramclone.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.instagramclone.ui.theme.LightBlack

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,navigation=buttons")
@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavHostController, currentDestination: NavDestination?) {

//    Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        NavigationBar(
            modifier = modifier
//                .navigationBarsPadding()
                .height(56.dp)
            ,
            containerColor = Color.White
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
                    },
                    colors = NavigationBarItemColors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = LightBlack,
                        selectedTextColor = Color.Black,
                        selectedIndicatorColor = Color.Transparent,
                        unselectedTextColor = Color.Black,
                        disabledIconColor = Color.Black,
                        disabledTextColor = Color.Black,
                    )
                )
            }
        }
//    }
}