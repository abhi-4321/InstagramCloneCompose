package com.example.instagramclone.screen.main

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.model.FollowUserItem
import com.example.instagramclone.model.TabItem
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.TransGray
import com.example.instagramclone.ui.theme.WhiteGray
import com.example.instagramclone.viewmodel.MainViewModel

@Composable
fun UserProfile(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController,
    userId: Int
) {

    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile(userId)
    }

    val flowState by viewModel.flowUserProfile.collectAsState()

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    val followingListState by viewModel.flowFollowingList.collectAsState()

    var isFollowing by remember {
        mutableStateOf(true)
    }

    if (followingListState is MainViewModel.ApiResponse.Success<FollowUserItem>) {
        val list =
            (followingListState as MainViewModel.ApiResponse.Success<FollowUserItem>).data?.list
                ?: emptyList()

        if (list.contains(userId)) {
            isFollowing = true
        } else {
            isFollowing = false
        }
    }

    when (flowState) {
        is MainViewModel.ApiResponse.Failure -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load. Retry", Modifier.clickable{
                    viewModel.fetchUserProfile(userId)
                }, color = Blue)
            }
        }

        MainViewModel.ApiResponse.Idle -> {}
        MainViewModel.ApiResponse.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier.size(40.dp))
            }
        }

        is MainViewModel.ApiResponse.Success<*> -> {
            val flow = (flowState as MainViewModel.ApiResponse.Success).data!!

            Log.d("ApiResponse", "Response Success with data :  $flow")

            Column(
                modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .background(Color.White)
            ) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            modifier = modifier,
                            tint = Color.Black
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = flow.username,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        modifier = modifier
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                Log.d("TAG", "TopAppBar: navigated")
                            },
                        tint = Color.Black
                    )
                }

                Row(
                    modifier
                        .height(90.dp)
                        .padding(horizontal = 15.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    RoundImage(
                        image = flow.profileImageUrl,
                        modifier = Modifier
                            .fillMaxHeight(0.95f)
                            .aspectRatio(1.05f)
                    )

                    Column(
                        modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 5.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Text(
                            text = flow.fullName,
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = flow.postsCount,
                                    color = Color.Black,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "posts",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = flow.followersCount,
                                    color = Color.Black,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "followers",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = flow.followingCount,
                                    color = Color.Black,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "following",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                ) {
                    Text(
                        text = flow.bio,
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    )
                }

                if (!isFollowing && flow.private) {
                    Spacer(Modifier.height(5.dp))
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .background(color = Blue, shape = RoundedCornerShape(8.dp)),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Follow",
                            Modifier.padding(vertical = 6.dp),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(Modifier.height(20.dp))

                    Row(
                        modifier
                            .height(70.dp)
                            .padding(horizontal = 15.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(15.dp)
                    ) {

                        Box(
                            Modifier
                                .size(60.dp)
                                .background(color = WhiteGray, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null
                            )
                        }

                        Column(
                            modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "This account is private",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Follow this account to see their photos and videos.",
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                } else {
                    ConstraintLayout(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp)
                    ) {
                        // Create references for the three elements
                        val (editProfile, shareProfile, addPerson) = createRefs()

                        // Create a height reference that will be shared
                        createGuidelineFromTop(0.5f)

                        // Edit Profile Button
                        Row(
                            modifier = Modifier
                                .constrainAs(editProfile) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    height =
                                        Dimension.wrapContent  // This height will be used as reference
                                    width =
                                        Dimension.percent(0.44f)  // Using percent instead of weight
                                }
                                .background(WhiteGray, RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isFollowing) "Following" else "Follow",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                            )
                            if (isFollowing)
                                Icon(
                                    Icons.Outlined.ArrowDropDown,
                                    contentDescription = null,
                                    modifier = Modifier.scale(0.95f)
                                )
                        }

                        // Share Profile Button
                        Row(
                            modifier = Modifier
                                .constrainAs(shareProfile) {
                                    start.linkTo(editProfile.end, margin = 5.dp)
                                    top.linkTo(editProfile.top)
                                    bottom.linkTo(editProfile.bottom)
                                    height =
                                        Dimension.fillToConstraints  // Match edit profile height
                                    width = Dimension.percent(0.44f)
                                }
                                .background(WhiteGray, RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Message",
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                            )
                        }

                        // Add Person Icon
                        Row(
                            modifier = Modifier
                                .constrainAs(addPerson) {
                                    start.linkTo(shareProfile.end, margin = 5.dp)
                                    top.linkTo(editProfile.top)
                                    bottom.linkTo(editProfile.bottom)
                                    height =
                                        Dimension.fillToConstraints  // Match edit profile height
                                    width = Dimension.percent(0.08f)
                                }
                                .background(WhiteGray, RoundedCornerShape(4.dp)),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_person_add_24),
                                contentDescription = null,
                                modifier = Modifier.scale(0.7f),
                                tint = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = modifier.height(20.dp))

                    LazyRow(
                        modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        state = rememberLazyListState()
                    ) {
                        items(flow.highlights.size) {
                            Spacer(modifier = modifier.width(15.dp))
                            Column(
                                modifier.wrapContentSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                RoundImage(
                                    image = flow.highlights[it].highlightUrl,
                                    modifier = modifier.size(60.dp)
                                )
                                Spacer(modifier = modifier.height(5.dp))
                                Text(
                                    text = flow.highlights[it].title,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = modifier.height(10.dp))

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .border(1.dp, TransGray, CutCornerShape(1.dp))
                    ) {}

                    val inactiveColor = Color.LightGray
                    val tabsList = listOf(
                        TabItem("Posts", painterResource(id = R.drawable.round_grid)),
                        TabItem("Profile", painterResource(id = R.drawable.baseline_account_box_24))
                    )

                    TabRow(
                        selectedTabIndex = selectedIndex,
                        containerColor = Color.Transparent,
                        contentColor = Color.Black,
                        modifier = modifier
                    ) {
                        tabsList.forEachIndexed { index, item ->
                            Tab(
                                selected = selectedIndex == index,
                                unselectedContentColor = inactiveColor,
                                selectedContentColor = Color.Black,
                                onClick = {
                                    selectedIndex = index
                                }
                            ) {
                                Icon(
                                    painter = item.image,
                                    contentDescription = item.title,
                                    modifier = modifier
                                        .padding(10.dp)
                                        .size(25.dp),
                                    tint = if (selectedIndex == index) Color.Black else inactiveColor
                                )
                            }
                        }
                    }

                    when (selectedIndex) {
                        0 -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                Modifier
                                    .fillMaxWidth()
                                    .scale(1.01f)
                            ) {
                                items(flow.posts.size) {
                                    AsyncImage(
                                        modifier = modifier
                                            .padding((0.75).dp)
                                            .size(180.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = null
                                            ) {
                                                navController.navigate(Screen.ViewPost(flow.posts[it].id,"Posts"))
                                            }
                                        ,
                                        model = flow.posts[it].postUrl.ifEmpty { R.drawable.ig },
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            }
                        }

                        1 -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                Modifier
                                    .fillMaxWidth()
                                    .scale(1.01f)
                            ) {
                                items(flow.posts.size) {
                                    AsyncImage(
                                        modifier = modifier
                                            .padding((0.75).dp)
                                            .size(180.dp)
                                            .clickable(
                                                indication = null,
                                                interactionSource = null
                                            ) {
                                                navController.navigate(Screen.ViewPost(flow.posts[it].id,"Posts"))
                                            }
                                        ,
                                        model = flow.posts[it].postUrl.ifEmpty { R.drawable.ig },
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UserProfilePreview() {
    UserProfile(
        navController = rememberNavController(),
        viewModel = MainViewModel(RetrofitInstanceMain.getApiService("")),
        userId = 0
    )
}