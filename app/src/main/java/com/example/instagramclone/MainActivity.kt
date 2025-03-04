package com.example.instagramclone

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.model.Profile
import com.example.instagramclone.screen.login.Birthday
import com.example.instagramclone.screen.login.Confirmation
import com.example.instagramclone.screen.Dashboard
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
        val viewModel by viewModels<MainViewModel>()

        window.statusBarColor = resources.getColor(R.color.white,theme) // Set background color
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Dark icons

        setContent {
            InstagramCloneTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Login) {
                    composable<Login> {
                        Login(navController = navController)
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
                        Profile()
                    }
                    composable<Dashboard> {
                        Dashboard()
                    }
                }
            }
        }
    }
}