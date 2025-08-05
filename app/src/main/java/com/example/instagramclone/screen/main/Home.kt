package com.example.instagramclone.screen.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.ui.theme.Pink
import com.example.instagramclone.ui.theme.TransGray
import com.example.instagramclone.ui.theme.WhiteGray
import com.example.instagramclone.viewmodel.MainViewModel
import com.example.instagramclone.viewmodel.StoryViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    storyViewModel: StoryViewModel,
    storyClicked: (displayUser: StoryDisplayUser) -> Unit
) {
    val context = LocalContext.current

    val displayListState by storyViewModel.liveDataStory.collectAsState()
    val postsListState by viewModel.liveDataFeed.collectAsState()

    val userDetailsState by viewModel.liveData.collectAsState()

    LaunchedEffect(Unit) {
        if (postsListState !is MainViewModel.ApiResponse.Success<*>) {
            viewModel.fetchFeed()
        }
    }

    var imageUrl by remember { mutableStateOf("") }

    var localLikedPosts by remember { mutableStateOf(setOf<Int>()) }
    var localLikeCounts by remember { mutableStateOf(mapOf<Int, Int>()) }
    val pendingApiCalls = remember { mutableStateMapOf<Int, Job>() }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit)
    {
        onDispose {
            pendingApiCalls.values.forEach { it.cancel() }
            pendingApiCalls.clear()
        }
    }

    // When userDetailsState updates, update imageUrl
    LaunchedEffect(userDetailsState) {
        if (userDetailsState is MainViewModel.ApiResponse.Success) {
            imageUrl =
                (userDetailsState as MainViewModel.ApiResponse.Success).data!!.profileImageUrl
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 35.dp, bottom = 60.dp),
    ) {

        item {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 15.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Instagram",
                    fontSize = 20.sp,
                    modifier = modifier,
                )
                Row {
                    Icon(
                        modifier = modifier.size(25.dp),
                        painter = painterResource(id = R.drawable.round_favorite_border_24),
                        contentDescription = null
                    )
                    Spacer(modifier.width(20.dp))
                    Icon(
                        modifier = modifier
                            .size(21.dp)
                            .padding(top = 3.dp)
                            .clickable {
                                navController.navigate(Screen.Messages)
                            },
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = null
                    )
                }
            }

            LazyRow(
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                state = rememberLazyListState()
            ) {
                when (displayListState) {
                    is MainViewModel.ApiResponse.Failure -> {
                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = if (imageUrl.isEmpty()) R.drawable.user else imageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(70.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Log.d("ResultApi", "List Display : failed")
                        Toast.makeText(
                            context,
                            "Failed to fetch the display users : ${(displayListState as MainViewModel.ApiResponse.Failure).error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    MainViewModel.ApiResponse.Idle -> {}
                    MainViewModel.ApiResponse.Loading -> {
                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier.wrapContentSize(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    AsyncImage(
                                        model = if (imageUrl.isEmpty()) R.drawable.user else imageUrl,
                                        contentDescription = "menu",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(70.dp)
                                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )
                                    Image(
                                        modifier = modifier
                                            .border(
                                                BorderStroke(2.dp, Color.White), shape = CircleShape
                                            )
                                            .size(20.dp),
                                        painter = painterResource(R.drawable.round_add_circle_24),
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Log.d("ResultApi", "List Display : loading")
                    }

                    is MainViewModel.ApiResponse.Success<*> -> {
                        val list = (displayListState as MainViewModel.ApiResponse.Success).data!!

                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier.wrapContentSize(),
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    AsyncImage(
                                        model = if (imageUrl.isEmpty()) R.drawable.user else imageUrl,
                                        contentDescription = "menu",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(70.dp)
                                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )
                                    Image(
                                        modifier = modifier
                                            .border(
                                                BorderStroke(2.dp, Color.White), shape = CircleShape
                                            )
                                            .size(20.dp),
                                        painter = painterResource(R.drawable.round_add_circle_24),
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Log.d("ResultApi", "List Display : $list")

                        items(list.size) {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier
                                    .wrapContentSize()
                                    .clickable {
                                        storyClicked(list[it])
                                        navController.navigate(
                                            Screen.Story(
                                                list[it].userId,
                                                list[it].profileImageUrl,
                                                list[it].username,
                                                list[it].fullName
                                            )
                                        ) {
                                            launchSingleTop = true
                                        }
                                    },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RoundImage(
                                    image = list[it].profileImageUrl,
                                    modifier = modifier.size(70.dp)
                                )
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = list[it].username,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }


            }

            Spacer(modifier.height(15.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .border(1.dp, TransGray, CutCornerShape(1.dp))
            ) {}
        }


        when (postsListState) {
            is MainViewModel.ApiResponse.Failure -> {
                Toast.makeText(
                    context,
                    "Failed to fetch the feed : ${(postsListState as MainViewModel.ApiResponse.Failure).error}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            MainViewModel.ApiResponse.Idle -> {}
            MainViewModel.ApiResponse.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp)
                            .height(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }

            is MainViewModel.ApiResponse.Success<*> -> {
                val postsList =
                    (postsListState as MainViewModel.ApiResponse.Success).data ?: emptyList()
                items(postsList) { post ->
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = rememberImagePainter(data = post.profileImageUrl) {
                                    error(
                                        R.drawable.p
                                    )
                                },
                                contentDescription = "menu",
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .size(40.dp)
                                    .border(
                                        BorderStroke(1.dp, Pink), shape = CircleShape
                                    )
                                    .padding(3.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = post.username,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = modifier.padding(horizontal = 10.dp)
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Follow",
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(
                                        color = WhiteGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                modifier = modifier.size(25.dp),
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                    }

                    AsyncImage(
                        model = post.postUrl,
                        contentDescription = "menu",
                        contentScale = ContentScale.Crop,
                        modifier = modifier
                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    )
                    Spacer(modifier = modifier.height(8.dp))

                    Row(
                        modifier = modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .height(28.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val uId = (userDetailsState as MainViewModel.ApiResponse.Success).data?.id!!
                            Icon(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .scale(1f)
                                    .clickable {
                                        val postId = post.id // Assuming your post has an id field
                                        val isCurrentlyLiked = post.likedBy.contains(uId) || localLikedPosts.contains(postId)

                                        // Immediately update UI state
                                        localLikedPosts = if (isCurrentlyLiked) {
                                            localLikedPosts - postId
                                        } else {
                                            localLikedPosts + postId
                                        }

                                        // Update local like count
                                        val currentCount = localLikeCounts[postId] ?: post.likesCount.toIntOrNull() ?: 0
                                        localLikeCounts = localLikeCounts + (postId to if (isCurrentlyLiked) currentCount - 1 else currentCount + 1)

                                        // Cancel any existing pending API call for this post
                                        pendingApiCalls[postId]?.cancel()

                                        // Start new 2-second timer for API call
                                        val apiJob = coroutineScope.launch {
                                            delay(2000) // 2 second delay

                                            // Make API call after delay
                                            viewModel.toggleLikePost(postId) { success ->
                                                if (!success) {
                                                    // Revert UI state on API failure
                                                    localLikedPosts = if (isCurrentlyLiked) {
                                                        localLikedPosts + postId
                                                    } else {
                                                        localLikedPosts - postId
                                                    }

                                                    // Revert like count
                                                    val revertCount = localLikeCounts[postId] ?: currentCount
                                                    localLikeCounts = localLikeCounts + (postId to if (isCurrentlyLiked) revertCount + 1 else revertCount - 1)

                                                    Toast.makeText(context, "Failed to update like", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    // Success - optionally refresh feed to get latest server state
                                                    // viewModel.fetchFeed()
                                                }
                                            }

                                            // Remove from pending calls
                                            pendingApiCalls.remove(postId)
                                        }

                                        // Store the job so it can be cancelled if needed
                                        pendingApiCalls[postId] = apiJob
                                    }
                                ,
                                painter = painterResource(
                                    id = if (post.likedBy.contains(uId) || localLikedPosts.contains(post.id))
                                            R.drawable.liked
                                        else
                                            R.drawable.notliked
                                ),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                            Spacer(modifier.width(4.dp))
                            Text(
                                (localLikeCounts[post.id] ?: post.likesCount.toIntOrNull() ?: 0).toString(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier.width(10.dp))
                            Icon(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .offset(y = (-0.5).dp)
                                    .clickable {
                                    },
                                painter = painterResource(id = R.drawable.comment),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                            Spacer(modifier.width(4.dp))
                            Text(
                                post.commentsCount,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier.width(10.dp))
                            Icon(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .scale(0.9f)
                                    .offset(y = 1.dp),
                                painter = painterResource(id = R.drawable.share),
                                tint = Color.Unspecified,
                                contentDescription = null,
                            )
                            Spacer(modifier.width(5.dp))
                            Text("7", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Icon(
                            modifier = modifier.fillMaxHeight(),
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier.height(5.dp))
                    Text(
                        "Liked by zufu.kid and ${(localLikeCounts[post.id] ?: post.likesCount.toIntOrNull() ?: 0)} others",
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 14.sp
                    )

                    Spacer(modifier.height(5.dp))
                    Text(
                        "${post.username} ${post.caption}",
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier.height(5.dp))
                    Text(
                        "View all ${post.commentsCount} comments",
                        color = Color(0xFF6F7680),
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 14.sp,
                    )

                    Spacer(modifier.height(5.dp))
                    Text(
                        "14 hours ago",
                        color = Color(0xFF6F7680),
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 13.sp,
                    )

                    Spacer(modifier.height(5.dp))
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    Home(
        navController = rememberNavController(),
        viewModel = MainViewModel(RetrofitInstanceMain.getApiService("")),
        storyViewModel = StoryViewModel(RetrofitInstanceMain.getApiService(""))
    ) { }
}