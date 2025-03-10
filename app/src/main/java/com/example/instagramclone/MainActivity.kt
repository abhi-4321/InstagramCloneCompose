package com.example.instagramclone

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
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
        val startDestination = if (savedToken != null) Screen.Home else Screen.Home

        val viewModel = ViewModelProvider(this)[MainViewModel::class]
        viewModel.initOrUpdateRetrofit(applicationContext)

        setContent {
            InstagramCloneTheme {
                val navController = rememberNavController()

                val slideTime = 300
                val slideLeftHorizontallyEnter =
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideTime))
                val slideLeftHorizontallyExit =
                    slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(slideTime))
                val slideRightHorizontallyPopEnter =
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideTime))
                val slideRightHorizontallyPopExit =
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideTime))

                val items = BottomBarDestinations.entries
                val currentDestination = navController.currentBackStackEntryAsState().value?.destination

                val showBottomBar = items.any { currentDestination?.hasRoute(it.screen::class) == true }

                Log.d("BottomBar", "Boolean 1: $showBottomBar")

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
                        popEnterTransition = { slideRightHorizontallyPopExit },
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable<Screen.Login> {
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
                        composable<Screen.Profile> {
                            Profile(viewModel = viewModel)
                        }
                        composable<Screen.Home> {
                            Home(navController = navController, viewModel = viewModel)
                        }
                        composable<Screen.Search> {
                            Search(navController = navController)
                        }
                        composable<Screen.Reels> {
                            Reels(navController = navController)
                        }
                        composable<Screen.Create> {
                            Create(navController = navController)
                        }
                    }
                }
            }
        }
    }
}