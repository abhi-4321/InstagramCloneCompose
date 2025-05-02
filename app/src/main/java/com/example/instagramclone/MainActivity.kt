package com.example.instagramclone

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.instagramclone.navigation.BottomBarDestinations
import com.example.instagramclone.navigation.BottomNavigationBar
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.screen.chat.Chat
import com.example.instagramclone.screen.chat.Messages
import com.example.instagramclone.screen.chat.NewMessage
import com.example.instagramclone.screen.main.Create
import com.example.instagramclone.screen.main.EditPost
import com.example.instagramclone.screen.main.Home
import com.example.instagramclone.screen.main.Profile
import com.example.instagramclone.screen.main.Reels
import com.example.instagramclone.screen.main.Search
import com.example.instagramclone.screen.util.Settings
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.viewmodel.MainViewModel
import com.example.instagramclone.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    var isPermitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                getColor(R.color.white),
                getColor(R.color.black)
            )
        )

        val token = intent.getStringExtra("token") ?: ""

        checkAndRequestPermission()

        val retrofitInterfaceMain = RetrofitInstanceMain.getApiService(token)
        val mainViewModelFactory = MainViewModelFactory(retrofitInterfaceMain)

        val viewModel by viewModels<MainViewModel> {
            mainViewModelFactory
        }

        viewModel.fetchUser()

        setContent {
            InstagramCloneTheme {
                Main(viewModel, retrofitInterfaceMain)
            }
        }
    }

    //@Preview(showSystemUi = true)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Main(viewModel: MainViewModel, retrofitInterfaceMain: RetrofitInterfaceMain) {
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
                    Create(navController = navController) {
                        return@Create loadImages()
                    }
                }
                composable<Screen.Settings> {
                    Settings(navController = navController, viewModel = viewModel) {
                        startActivity(Intent(this@MainActivity,LoginActivity::class.java))
                        finish()
                    }
                }
                composable<Screen.Messages> {
                    Messages(navController = navController, viewModel = viewModel)
                }
                composable<Screen.Chat> {
                    val args = it.toRoute<Screen.Chat>()
                    Chat(receiverId = args.receiverId, profileImageUrl = args.profileImageUrl, fullName = args.fullName, username = args.username , senderId = args.senderId, retrofitInterfaceMain = retrofitInterfaceMain, mainViewModel = viewModel)
                }
                composable<Screen.NewMessage> {
                    NewMessage(viewModel = viewModel, navController = navController)
                }

                composable<Screen.EditPost> {
                    val args = it.toRoute<Screen.EditPost>()
                    EditPost(navController = navController, uri = args.uri)
                }
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                isPermitted = true
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                isPermitted = false
            }
        }

    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            isPermitted = true
        }
    }

    fun loadImages() : List<Uri> {
        if (!isPermitted)
            return emptyList()

        val imageList = mutableListOf<Uri>()

        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )
                imageList.add(contentUri)
            }
        }

        return imageList
    }
}