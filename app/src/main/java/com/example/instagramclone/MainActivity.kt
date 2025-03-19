package com.example.instagramclone

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.screen.main.Home
import com.example.instagramclone.screen.main.Profile
import com.example.instagramclone.screen.login.Birthday
import com.example.instagramclone.screen.login.Confirmation
import com.example.instagramclone.screen.login.EnterPassword
import com.example.instagramclone.screen.login.Login
import com.example.instagramclone.screen.login.Name
import com.example.instagramclone.screen.login.ProfilePicture
import com.example.instagramclone.screen.login.Register
import com.example.instagramclone.screen.login.SaveInfo
import com.example.instagramclone.screen.login.TermsAndPolicies
import com.example.instagramclone.screen.login.Username
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.navigation.NavDestination.Companion.hasRoute
import com.example.instagramclone.navigation.BottomBarDestinations
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.navigation.BottomNavigationBar
import com.example.instagramclone.screen.main.Create
import com.example.instagramclone.screen.main.Reels
import com.example.instagramclone.screen.main.Search
import com.example.instagramclone.screen.util.Settings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        var keepSplashScreen = true
        splashscreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        lifecycleScope.launch {
            delay(2000)
            keepSplashScreen = false
        }

        val sessionManager = SessionManager(this)
        val savedToken = sessionManager.fetchAuthToken()
        val startDestination = if (savedToken != null) Screen.Home else Screen.Login

        val viewModel = ViewModelProvider(this)[MainViewModel::class]
        viewModel.initOrUpdateRetrofit(applicationContext)

        setContent {
            InstagramCloneTheme {
                Main(startDestination,viewModel)
            }
        }
    }

//    @Preview(showSystemUi = true)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Main(startDestination: Screen, viewModel: MainViewModel) {
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
                startDestination = startDestination,
                enterTransition = { slideLeftHorizontallyEnter },
                exitTransition = { slideLeftHorizontallyExit },
                popExitTransition = { slideRightHorizontallyPopEnter },
                popEnterTransition = { slideRightHorizontallyPopExit }
            ) {
                composable<Screen.Login>(
                    enterTransition = { null },
                    popExitTransition = { null }
                ) {
                    Login(navController = navController, viewModel = viewModel)
                }
                composable<Screen.Register> {
                    Register(navController = navController)
                }
                composable<Screen.Confirmation> {
                    Confirmation(navController = navController)
                }
                composable<Screen.EnterPassword> {
                    EnterPassword(navController = navController)
                }
                composable<Screen.SaveInfo> {
                    SaveInfo(navController = navController)
                }
                composable<Screen.Birthday> {
                    Birthday(navController = navController)
                }
                composable<Screen.Name> {
                    Name(navController = navController)
                }
                composable<Screen.Username> {
                    Username(navController = navController)
                }
                composable<Screen.TermsAndPolicies> {
                    TermsAndPolicies(navController = navController)
                }
                composable<Screen.ProfilePicture> {
                    ProfilePicture(navController = navController)
                }
                composable<Screen.Profile>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Profile(viewModel = viewModel, navController = navController)
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
                    Settings(navController = navController)
                }
            }
        }
    }
}