package com.example.instagramclone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.navigation.BottomBarDestinations
import com.example.instagramclone.navigation.BottomNavigationBar
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.screen.main.Create
import com.example.instagramclone.screen.main.Home
import com.example.instagramclone.screen.main.Profile
import com.example.instagramclone.screen.main.Reels
import com.example.instagramclone.screen.main.Search
import com.example.instagramclone.screen.util.Settings
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.viewmodel.MainViewModel
import com.example.instagramclone.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(R.color.white),
                getColor(R.color.black)
            )
        )

        val token = intent.getStringExtra("token") ?: ""

        val retrofitInterfaceMain = RetrofitInstanceMain.getApiService(token)
        val mainViewModelFactory = MainViewModelFactory(retrofitInterfaceMain)

        val viewModel by viewModels<MainViewModel> {
            mainViewModelFactory
        }

        viewModel.fetchUser()

        setContent {
            InstagramCloneTheme {
                Main(viewModel)
            }
        }

    }

    //@Preview(showSystemUi = true)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Main(viewModel: MainViewModel) {
        val navController = rememberNavController()

        val slideTime = 300
        val slideLeftHorizontallyEnter = // New screen coming left from right after new screen enter
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideTime))
        val slideLeftHorizontallyExit =
            // Current screen going left from right after new screen enter
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(slideTime))
        val slideRightHorizontallyPopEnter =
            // Previous screen coming right from left after back press
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideTime))
        val slideRightHorizontallyPopExit = // Current screen going right from left after back press
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideTime))

        val items = BottomBarDestinations.entries
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        val showBottomBar = items.any { currentDestination?.hasRoute(it.screen::class) == true }

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(
                        navController = navController,
                        currentDestination = currentDestination
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home,
                enterTransition = { slideLeftHorizontallyEnter },
                exitTransition = { slideLeftHorizontallyExit },
                popExitTransition = { slideRightHorizontallyPopEnter },
                popEnterTransition = { slideRightHorizontallyPopExit }
            ) {
                composable<Screen.Profile>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Profile(viewModel = viewModel, navController = navController) {
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                        finish()
                    }
                }
                composable<Screen.Home>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Home(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable<Screen.Search>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Search(navController = navController)
                }
                composable<Screen.Reels>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Reels(navController = navController)
                }
                composable<Screen.Create>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Create(navController = navController)
                }
                composable<Screen.Settings> {
                    Settings(navController = navController, viewModel = viewModel) {
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }
}