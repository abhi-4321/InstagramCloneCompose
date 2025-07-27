package com.example.instagramclone

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.navigation.BottomBarDestinations
import com.example.instagramclone.navigation.BottomNavigationBar
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.screen.chat.Chat
import com.example.instagramclone.screen.chat.Messages
import com.example.instagramclone.screen.chat.NewMessage
import com.example.instagramclone.screen.main.AddHighlight
import com.example.instagramclone.screen.main.Create
import com.example.instagramclone.screen.main.EditPost
import com.example.instagramclone.screen.main.HighlightTitle
import com.example.instagramclone.screen.main.Home
import com.example.instagramclone.screen.main.MyProfile
import com.example.instagramclone.screen.main.Reels
import com.example.instagramclone.screen.main.Search
import com.example.instagramclone.screen.main.Story
import com.example.instagramclone.screen.main.ViewPost
import com.example.instagramclone.screen.util.Settings
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.viewmodel.MainViewModel
import com.example.instagramclone.viewmodel.MainViewModelFactory
import com.example.instagramclone.viewmodel.StoryViewModel
import com.example.instagramclone.viewmodel.StoryViewModelFactory
import com.example.instagramclone.worker.PostWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    var isPermitted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.Transparent.toArgb()
        window.navigationBarColor = Color.Transparent.toArgb()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        val token = intent.getStringExtra("token") ?: ""

        checkAndRequestPermission()

        val retrofitInterfaceMain = RetrofitInstanceMain.getApiService(token)
        val mainViewModelFactory = MainViewModelFactory(retrofitInterfaceMain)
        val storyViewModelFactory = StoryViewModelFactory(retrofitInterfaceMain)

        val viewModel by viewModels<MainViewModel> {
            mainViewModelFactory
        }

        val storyViewModel by viewModels<StoryViewModel> {
            storyViewModelFactory
        }

        viewModel.fetchUser()
        storyViewModel.fetchDisplayUsers()

        setContent {
            InstagramCloneTheme {
                Main(viewModel, retrofitInterfaceMain, token, storyViewModel)
            }
        }
    }

    //@Preview(showSystemUi = true)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun Main(
        viewModel: MainViewModel,
        retrofitInterfaceMain: RetrofitInterfaceMain,
        token: String,
        storyViewModel: StoryViewModel
    ) {
        val navController = rememberNavController()
        val storyListState = storyViewModel.liveDataStory.collectAsState()

        var list by remember {
            mutableStateOf(emptyList<StoryDisplayUser>())
        }

        var currentDuIndex by remember {
            mutableIntStateOf(-1)
        }

        LaunchedEffect(storyListState.value) {
            when (storyListState.value) {
                is MainViewModel.ApiResponse.Failure -> {
                    currentDuIndex = -1
                }

                MainViewModel.ApiResponse.Idle -> {}
                MainViewModel.ApiResponse.Loading -> {}
                is MainViewModel.ApiResponse.Success<List<StoryDisplayUser>> -> {
                    list =
                        (storyListState.value as MainViewModel.ApiResponse.Success<List<StoryDisplayUser>>).data!!
                    currentDuIndex = 0
                }
            }
        }

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
            containerColor = Color.White, // Important: Set background color explicitly
            contentColor = Color.Black,
            bottomBar = {
                if (showBottomBar && currentDestination?.hasRoute(Screen.Create::class) == false) {
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
                popEnterTransition = { slideRightHorizontallyPopExit },
            ) {
                composable<Screen.Profile>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    MyProfile(viewModel = viewModel, navController = navController) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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
                        viewModel = viewModel,
                        storyViewModel = storyViewModel
                    ) { displayUser ->
                        currentDuIndex = list.indexOfFirst { it.userId == displayUser.userId }
                    }
                }
                composable<Screen.Search>(
                    enterTransition = { null },
                    popExitTransition = { null },
                    exitTransition = { null },
                    popEnterTransition = { null }
                ) {
                    Search(navController = navController, viewModel = viewModel)
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
                    Create(navController = navController) { offset ->
                        return@Create loadImageThumbnails(offset)
                    }
                }
                composable<Screen.Settings> {
                    Settings(navController = navController, viewModel = viewModel) {
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                }
                composable<Screen.Messages> {
                    Messages(navController = navController, viewModel = viewModel)
                }
                composable<Screen.Chat> {
                    val args = it.toRoute<Screen.Chat>()
                    Chat(
                        receiverId = args.receiverId,
                        profileImageUrl = args.profileImageUrl,
                        fullName = args.fullName,
                        username = args.username,
                        senderId = args.senderId,
                        retrofitInterfaceMain = retrofitInterfaceMain,
                        mainViewModel = viewModel
                    )
                }
                composable<Screen.NewMessage> {
                    NewMessage(viewModel = viewModel, navController = navController)
                }

                composable<Screen.EditPost> {
                    val args = it.toRoute<Screen.EditPost>()
                    EditPost(navController = navController, uri = args.uri) { caption, imageUri ->

                        val inputData = workDataOf(
                            "caption" to caption,
                            "uri" to imageUri,
                            "token" to token
                        )

                        val uuid = UUID.randomUUID()

                        val workRequest =
                            OneTimeWorkRequestBuilder<PostWorker>().setInputData(inputData)
                                .setId(uuid).build()

                        val workManager = WorkManager.getInstance(this@MainActivity)
                        workManager.enqueueUniqueWork(
                            Random.nextInt().toString(),
                            ExistingWorkPolicy.APPEND, workRequest
                        )

                        lifecycleScope.launch {
                            workManager.getWorkInfoByIdFlow(uuid).collectLatest {
                                when (it?.state) {
                                    WorkInfo.State.ENQUEUED -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Creating post",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    WorkInfo.State.SUCCEEDED -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Post created successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.fetchUser()
                                    }

                                    WorkInfo.State.FAILED, WorkInfo.State.CANCELLED -> {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Error in creating post : ${it.stopReason}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> {}
                                }
                            }
                        }
                    }
                }

                composable<Screen.Story> {
                    val args = it.toRoute<Screen.Story>()

                    Story(
                        userId = args.userId,
                        username = args.username,
                        fullName = args.fullName,
                        profileImageUrl = args.profileImageUrl,
                        viewModel = viewModel,
                        onNavigateToNextUser = {
                            if (currentDuIndex < list.size - 1) {
                                currentDuIndex++
                                navController.navigate(
                                    route = Screen.Story(
                                        list[currentDuIndex].userId,
                                        list[currentDuIndex].profileImageUrl,
                                        list[currentDuIndex].username,
                                        list[currentDuIndex].fullName
                                    ),
                                ) {
                                    launchSingleTop = true
                                }
                            } else {
                                currentDuIndex = -1
                                navController.navigateUp()
                            }
                        },
                        onNavigateToPreviousUser = {
                            if (currentDuIndex > 0) {
                                currentDuIndex--
                                navController.navigate(
                                    route = Screen.Story(
                                        list[currentDuIndex].userId,
                                        list[currentDuIndex].profileImageUrl,
                                        list[currentDuIndex].username,
                                        list[currentDuIndex].fullName
                                    ),
                                ) {
                                    launchSingleTop = true
                                }
                            }
                        }
                    )
                }

                composable<Screen.AddHighLight> {
                    AddHighlight()
                }

                composable<Screen.HighlightTitle> {
                    val args = it.toRoute<Screen.HighlightTitle>()
                    HighlightTitle(args.url)
                }

                composable<Screen.ViewPost> {
                    val args = it.toRoute<Screen.ViewPost>()
                    ViewPost(args.postId,viewModel,navController,args.from)
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
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_MEDIA_IMAGES
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            isPermitted = true
        }
    }

    suspend fun loadImageThumbnails(offset: Int = 0, limit: Int = 12): List<Pair<Uri, Bitmap?>> {
        val thumbnailList = mutableListOf<Pair<Uri, Bitmap?>>()

        // Define the projection
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        // For Android 10 (API 29) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val queryArgs = Bundle().apply {
                putInt(ContentResolver.QUERY_ARG_LIMIT, limit)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
                putStringArray(
                    ContentResolver.QUERY_ARG_SORT_COLUMNS,
                    arrayOf(MediaStore.Images.Media.DATE_ADDED)
                )
                putInt(
                    ContentResolver.QUERY_ARG_SORT_DIRECTION,
                    ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                )
            }

            val query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                queryArgs,
                null
            )

            query?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    // Load a thumbnail instead of the full image
                    val thumbnail = getThumbnail(contentUri, 150)
                    thumbnailList.add(Pair(contentUri, thumbnail))
                }
            }
        } else {
            // For older Android versions
            val query = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                sortOrder
            )

            query?.use { cursor ->
                if (offset > 0 && cursor.count >= offset) {
                    cursor.moveToPosition(offset - 1)
                }

                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                var count = 0

                while (cursor.moveToNext() && count < limit) {
                    val id = cursor.getLong(idColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    // Load a thumbnail instead of the full image
                    val thumbnail = getThumbnail(contentUri, 150)
                    thumbnailList.add(Pair(contentUri, thumbnail))
                    count++
                }
            }
        }

        return thumbnailList
    }

    // Helper function to get thumbnails efficiently
    private fun getThumbnail(uri: Uri, size: Int): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use the built-in thumbnail API for Android 10+
                contentResolver.loadThumbnail(uri, Size(size, size), null)
            } else {
                // For older versions, use the MediaStore thumbnail API or create your own
                val thumbnailId = getImageThumbnailId(uri)
                if (thumbnailId != null) {
                    MediaStore.Images.Thumbnails.getThumbnail(
                        contentResolver,
                        thumbnailId,
                        MediaStore.Images.Thumbnails.MINI_KIND,
                        null
                    )
                } else {
                    // Fallback - load and resize the image
                    createThumbnailFromUri(uri, size)
                }
            }
        } catch (e: Exception) {
            Log.e("ImageLoader", "Error loading thumbnail: ${e.message}")
            null
        }
    }

    // Helper for older Android versions
    private fun getImageThumbnailId(imageUri: Uri): Long? {
        val id = imageUri.lastPathSegment?.toLongOrNull()
        return id
    }

    // Create a thumbnail by loading and resizing the full image (as fallback)
    // Use this only if necessary as it's resource-intensive
    private fun createThumbnailFromUri(uri: Uri, size: Int): Bitmap? {
        return try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                // First decode with inJustDecodeBounds=true to check dimensions
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeStream(inputStream, null, options)

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options, size, size)

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false

                // Reopen the stream since we've consumed it
                contentResolver.openInputStream(uri)?.use { newInputStream ->
                    BitmapFactory.decodeStream(newInputStream, null, options)
                }
            }
        } catch (e: Exception) {
            Log.e("ImageLoader", "Error creating thumbnail: ${e.message}")
            null
        }
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}