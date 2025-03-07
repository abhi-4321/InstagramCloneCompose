package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.screen.Dashboard
import com.example.instagramclone.screen.Profile
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
        val startDestination = if (savedToken != null) Dashboard else Login

        val retrofitInstanceMain = RetrofitInstanceMain.getApiService(this)
        val viewModel = ViewModelProvider(this,MainViewModel.MainViewModelFactory(retrofitInstanceMain))[MainViewModel::class]

        setContent {
            InstagramCloneTheme {
                val navController = rememberNavController()

                val slideTime = 500
                val slideLeftHorizontallyEnter =
                    slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideTime))
                val slideLeftHorizontallyExit =
                    slideOutHorizontally(targetOffsetX = { -it } , animationSpec = tween(slideTime))
                val slideRightHorizontallyPopEnter =
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideTime))
                val slideRightHorizontallyPopExit =
                    slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideTime))

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = { slideLeftHorizontallyEnter },
                    exitTransition = { slideLeftHorizontallyExit },
                    popExitTransition = { slideRightHorizontallyPopEnter },
                    popEnterTransition = { slideRightHorizontallyPopExit }
                ) {
                    composable<Login> {
                        Login(navController = navController, viewModel = viewModel) {
                            restartActivity()
                        }
                    }
                    composable<Register> {
                        Register(navController = navController)
                    }
                    composable<Confirmation> {
                        Confirmation(navController = navController)
                    }
                    composable<EnterPassword> {
                        EnterPassword(navController = navController)
                    }
                    composable<SaveInfo> {
                        SaveInfo(navController = navController)
                    }
                    composable<Birthday> {
                        Birthday(navController = navController)
                    }
                    composable<Name> {
                        Name(navController = navController)
                    }
                    composable<Username> {
                        Username(navController = navController)
                    }
                    composable<TermsAndPolicies> {
                        TermsAndPolicies(navController = navController)
                    }
                    composable<ProfilePicture> {
                        ProfilePicture(navController = navController)
                    }
                    composable<Profile> {
                        Profile(viewModel = viewModel, navController = navController)
                    }
                    composable<Dashboard> {
                        Dashboard(navController = navController) {
                            restartActivity()
                        }
                    }
                }
            }
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(Intent(this,MainActivity::class.java))
    }
}