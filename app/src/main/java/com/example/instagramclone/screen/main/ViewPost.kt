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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.ui.theme.Pink
import com.example.instagramclone.ui.theme.WhiteGray
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ViewPost(postId: Int, viewModel: MainViewModel, navController: NavController, from: String) {

    val userDetailsState by viewModel.liveData.collectAsState()
    val feedState by viewModel.liveDataFeed.collectAsState()

    var localLikedPosts by remember { mutableStateOf(setOf<Int>()) }
    var localLikeCounts by remember { mutableStateOf(mapOf<Int, Int>()) }
    val pendingApiCalls = remember { mutableStateMapOf<Int, Job>() }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val post = remember(feedState, postId) {
        when (feedState) {
            is MainViewModel.ApiResponse.Success -> {
                (feedState as MainViewModel.ApiResponse.Success<List<PostDisplay>>).data?.find { it.id == postId }
            }

            else -> null
        }
    }

    if (post == null) {
        when (feedState) {
            is MainViewModel.ApiResponse.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is MainViewModel.ApiResponse.Failure -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                        Modifier
                            .padding(10.dp)
                            .size(25.dp)
                            .align(Alignment.TopStart)
                            .clickable {
                                navController.navigateUp()
                            }
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Failed to load post")
                        Button(onClick = {
                            when (from) {
                                "Explore", "Profile" -> viewModel.fetchFeed()
                            }
                        }) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {}
        }
        return
    }

    Column(Modifier.statusBarsPadding().fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            navController.navigateUp()
                        },
                    tint = Color.Black
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (from == "Explore") "Explore" else "Posts",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = post.profileImageUrl,
                    contentDescription = "menu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
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
                    modifier = Modifier.padding(horizontal = 10.dp)
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
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        }

        Image(
            painter = rememberImagePainter(data = post.postUrl),
            contentDescription = "menu",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val uId = (userDetailsState as MainViewModel.ApiResponse.Success).data?.id!!
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .scale(1f)
                        .clickable {
                            val postId = post.id // Assuming your post has an id field
                            val isCurrentlyLiked =
                                post.likedBy.contains(uId) || localLikedPosts.contains(postId)

                            // Immediately update UI state
                            localLikedPosts = if (isCurrentlyLiked) {
                                localLikedPosts - postId
                            } else {
                                localLikedPosts + postId
                            }

                            // Update local like count
                            val currentCount =
                                localLikeCounts[postId] ?: post.likesCount.toIntOrNull() ?: 0
                            localLikeCounts =
                                localLikeCounts + (postId to if (isCurrentlyLiked) currentCount - 1 else currentCount + 1)

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
                                        localLikeCounts =
                                            localLikeCounts + (postId to if (isCurrentlyLiked) revertCount + 1 else revertCount - 1)

                                        Toast.makeText(
                                            context,
                                            "Failed to update like",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                        },
                    painter = painterResource(
                        id = if (post.likedBy.contains(uId) || localLikedPosts.contains(post.id))
                            R.drawable.liked
                        else
                            R.drawable.notliked
                    ),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    (localLikeCounts[post.id] ?: post.likesCount.toIntOrNull() ?: 0).toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .offset(y = (-0.5).dp)
                        .clickable {
                        },
                    painter = painterResource(id = R.drawable.comment),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    post.commentsCount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .fillMaxHeight()
                        .scale(0.9f)
                        .offset(y = 1.dp),
                    painter = painterResource(id = R.drawable.share),
                    tint = Color.Unspecified,
                    contentDescription = null,
                )
                Spacer(Modifier.width(5.dp))
                Text("7", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
            Icon(
                modifier = Modifier.fillMaxHeight(),
                painter = painterResource(id = R.drawable.save),
                contentDescription = null
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            "Liked by zufu.kid and ${(localLikeCounts[post.id] ?: post.likesCount.toIntOrNull() ?: 0)} others",
            modifier = Modifier.padding(horizontal = 15.dp),
            fontSize = 14.sp
        )

        Spacer(Modifier.height(5.dp))
        Text(
            "${post.username} ${post.caption}",
            modifier = Modifier.padding(horizontal = 15.dp),
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(Modifier.height(5.dp))
        Text(
            "View all ${post.commentsCount} comments",
            color = Color(0xFF6F7680),
            modifier = Modifier.padding(horizontal = 15.dp),
            fontSize = 14.sp,
        )

        Spacer(Modifier.height(5.dp))
        Text(
            "14 hours ago",
            color = Color(0xFF6F7680),
            modifier = Modifier.padding(horizontal = 15.dp),
            fontSize = 13.sp,
        )

    }
}

@Preview(showSystemUi = true)
@Composable
fun ViewPostPreview() {
    ViewPost(
        1,
        MainViewModel(RetrofitInstanceMain.getApiService("")),
        rememberNavController(),
        "Explore"
    )
}
