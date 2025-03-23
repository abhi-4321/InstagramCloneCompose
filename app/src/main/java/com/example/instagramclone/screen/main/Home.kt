package com.example.instagramclone.screen.main

import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.model.PostDisplay
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.ui.theme.Pink
import com.example.instagramclone.viewmodel.MainViewModel

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Home(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    val context = LocalContext.current

//    viewModel.resetLoginState()

    val displayListState by viewModel.liveDataStory.collectAsState()
    val postsListState by viewModel.liveDataFeed.collectAsState()

    val userDetailsState by viewModel.liveData.collectAsState()

    LaunchedEffect(Unit) {
        if (postsListState !is MainViewModel.ApiResponse.Success<*>) {
            viewModel.fetchFeed()
        }
    }

    var imageUrl by remember { mutableStateOf("") }

    // When userDetailsState updates, update imageUrl
    LaunchedEffect(userDetailsState) {
        if (userDetailsState is MainViewModel.ApiResponse.Success) {
            imageUrl = (userDetailsState as MainViewModel.ApiResponse.Success).data!!.profileImageUrl
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding()
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
                            .padding(top = 3.dp),
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
                when(displayListState) {
                    is MainViewModel.ApiResponse.Failure -> {
                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RoundImage(
                                    image = "",
                                    modifier = modifier.size(70.dp)
                                )
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Log.d("ResultApi","List Display : failed")
                        Toast.makeText(context,"Failed to fetch the display users : ${(displayListState as MainViewModel.ApiResponse.Failure).error}",Toast.LENGTH_SHORT).show()
                    }
                    MainViewModel.ApiResponse.Idle -> {}
                    MainViewModel.ApiResponse.Loading -> {
                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier.wrapContentSize(), contentAlignment = Alignment.BottomEnd) {
                                    Image(
                                        painter = rememberImagePainter(data = imageUrl),
                                        contentDescription = "menu",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(70.dp)
                                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )
                                    Image(modifier = modifier.border(
                                        BorderStroke(2.dp,Color.White), shape = CircleShape
                                    ), painter = painterResource(R.drawable.add_circle), contentDescription = null)
                                }
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Log.d("ResultApi","List Display : loading")
                    }
                    is MainViewModel.ApiResponse.Success<*> -> {
                        val list = (displayListState as MainViewModel.ApiResponse.Success).data!!

                        item {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier.wrapContentSize(), contentAlignment = Alignment.BottomEnd) {
                                    Image(
                                        painter = rememberImagePainter(data = imageUrl),
                                        contentDescription = "menu",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(70.dp)
                                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )
                                    Image(modifier = modifier.border(
                                        BorderStroke(2.dp,Color.White), shape = CircleShape
                                    ), painter = painterResource(R.drawable.add_circle), contentDescription = null)
                                }
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = "Your Story",
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Log.d("ResultApi","List Display : $list")

                        items(list.size) {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
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
            Line()
        }


        when(postsListState) {
            is MainViewModel.ApiResponse.Failure -> {
                Toast.makeText(context,"Failed to fetch the feed : ${(postsListState as MainViewModel.ApiResponse.Failure).error}",Toast.LENGTH_SHORT).show()
            }
            MainViewModel.ApiResponse.Idle -> {}
            MainViewModel.ApiResponse.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
               val postsList = (postsListState as MainViewModel.ApiResponse.Success).data ?: emptyList()
                items(postsList.size) {
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
//                        painter = painterResource(id = R.drawable.p),
                                painter = rememberImagePainter(data = postsList[it].profileImageUrl) {
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
                                text = postsList[it].username,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = modifier.padding(horizontal = 10.dp)
                            )
                        }
                        Icon(
                            modifier = modifier.size(25.dp),
                            painter = painterResource(id = R.drawable.baseline_more_horiz_24),
                            contentDescription = null
                        )
                    }

                    Image(
//                painter = painterResource(id = R.drawable.p),
                        painter = rememberImagePainter(data = postsList[it].postUrl),
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
                            Icon(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .scale(0.9f),
                                painter = painterResource(id = R.drawable.notliked),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                            Spacer(modifier.width(3.dp))
                            Text(
                                postsList[it].likesCount,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier.width(10.dp))
                            Icon(
                                modifier = modifier
                                    .fillMaxHeight()
                                    .offset(y = (-0.5).dp),
                                painter = painterResource(id = R.drawable.comment),
                                tint = Color.Unspecified,
                                contentDescription = null
                            )
                            Spacer(modifier.width(3.dp))
                            Text(
                                postsList[it].commentsCount,
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
                        "Liked by zufu.kid and ${postsList[it].likesCount} others",
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 14.sp
                    )

                    Spacer(modifier.height(5.dp))
                    Text(
                        "${postsList[it].username} ${postsList[it].caption}",
                        modifier = modifier.padding(horizontal = 15.dp),
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier.height(5.dp))
                    Text(
                        "View all ${postsList[it].commentsCount} comments",
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