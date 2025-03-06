package com.example.instagramclone

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.ViewModelProvider
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = resources.getColor(R.color.white,theme) // Set background color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Dark icons

        val sessionManager = SessionManager(this)
        val savedToken = sessionManager.fetchAuthToken()
        val startDestination = if (savedToken != null) Dashboard else Login

        val retrofitInstanceMain = RetrofitInstanceMain().getApiService(this)
        val viewModel = ViewModelProvider(this,MainViewModel.MainViewModelFactory(retrofitInstanceMain, sessionManager))[MainViewModel::class]

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
                        Login(navController = navController, viewModel = viewModel)
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
                        viewModel.fetchUser()
                        Profile(viewModel = viewModel, navController = navController)
                    }
                    composable<Dashboard> {
                        Dashboard(navController = navController)
                    }
                }
            }
        }
    }
}