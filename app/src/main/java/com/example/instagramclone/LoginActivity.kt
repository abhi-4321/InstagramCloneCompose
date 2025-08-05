package com.example.instagramclone

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.screen.login.Birthday
import com.example.instagramclone.screen.login.Confirmation
import com.example.instagramclone.screen.login.EnterPassword
import com.example.instagramclone.screen.login.ForgotPassword
import com.example.instagramclone.screen.login.Login
import com.example.instagramclone.screen.login.Name
import com.example.instagramclone.screen.login.ProfilePicture
import com.example.instagramclone.screen.login.Register
import com.example.instagramclone.screen.login.SaveInfo
import com.example.instagramclone.screen.login.TermsAndPolicies
import com.example.instagramclone.screen.login.Username
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(R.color.white),
                getColor(R.color.black)
            )
        )

        checkAndRequestPermission()

        val loginViewModel: LoginViewModel by viewModel()

        var keepSplashScreen = true

        splashscreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        val token = SessionManager.fetchToken(applicationContext)
        if (!token.isNullOrEmpty()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                putExtra("token", token)
            })
            finish()
        }
        keepSplashScreen = false

        setContent {
            InstagramCloneTheme {
                Login(loginViewModel)
            }
        }
    }

    @Composable
    fun Login(loginViewModel: LoginViewModel) {
        val navController = rememberNavController()

        val slideTime = 300
        val slideLeftHorizontallyEnter =
            // New screen coming left from right after new screen enter
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(slideTime))
        val slideLeftHorizontallyExit =
            // Current screen going left from right after new screen enter
            slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(slideTime))
        val slideRightHorizontallyPopEnter =
            // Previous screen coming right from left after back press
            slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(slideTime))
        val slideRightHorizontallyPopExit =
            // Current screen going right from left after back press
            slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(slideTime))

        NavHost(
            navController = navController,
            startDestination = Screen.Login,
            enterTransition = { slideLeftHorizontallyEnter },
            exitTransition = { slideLeftHorizontallyExit },
            popExitTransition = { slideRightHorizontallyPopEnter },
            popEnterTransition = { slideRightHorizontallyPopExit }
        ) {
            composable<Screen.Login>(
                enterTransition = { null },
                popExitTransition = { null }
            ) {
                Login(navController = navController, viewModel = loginViewModel) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        ).apply {
                            putExtra("token", it)
                        })
                    finish()
                }
            }
            composable<Screen.Register> {
                Register(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.Confirmation> {
                Confirmation(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.EnterPassword> {
                EnterPassword(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.SaveInfo> {
                SaveInfo(navController = navController)
            }
            composable<Screen.Birthday> {
                Birthday(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.Name> {
                Name(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.Username> {
                Username(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.TermsAndPolicies> {
                TermsAndPolicies(navController = navController, viewModel = loginViewModel)
            }
            composable<Screen.ProfilePicture> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.ProfilePicture>()
                ProfilePicture(navController = navController) {
                    startActivity(
                        Intent(
                            this@LoginActivity,
                            MainActivity::class.java
                        ).apply {
                            putExtra("token", args.token)
                        })
                    finish()
                }
            }
            composable<Screen.ForgotPassword> {
                ForgotPassword(navController = navController)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (!isGranted) {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    }
}