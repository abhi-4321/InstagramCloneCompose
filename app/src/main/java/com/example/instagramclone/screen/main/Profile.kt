package com.example.instagramclone.screen.main

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.model.HighlightItem
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.ProfileItem
import com.example.instagramclone.model.TabItem
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.TransGray
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.flow.flow

@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
fun Profile(modifier: Modifier = Modifier, viewModel: MainViewModel, navController: NavController, launchActivity: () -> Unit) {

    val context = LocalContext.current
    val flowState by viewModel.liveData.collectAsState()

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    when(flowState) {
        is MainViewModel.ApiResponse.Failure -> {
            Toast.makeText(context,"Failed to fetch the feed : ${(flowState as MainViewModel.ApiResponse.Failure).error}",Toast.LENGTH_SHORT).show()
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(modifier.wrapContentSize(), verticalArrangement = Arrangement.Center) {

                    Button(
                        onClick = {
                            viewModel.fetchUser()
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            modifier = modifier.padding(vertical = 4.dp),
                            text = "Retry",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                        )
                    }

                    Spacer(modifier.height(20.dp))

                    Button(
                        onClick = {
                            launchActivity()
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = ButtonDefaults.buttonColors(containerColor = Blue),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            modifier = modifier.padding(vertical = 4.dp),
                            text = "Log out",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                        )
                    }
                }
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
                TopAppBar(flow.username, navController = navController)
                UserInfo(flow)
                Bio(flow.bio)
                Options()
                Spacer(modifier = modifier.height(20.dp))
                Highlights(flow.highlights)
                Spacer(modifier = modifier.height(10.dp))
                Line()
                TabView(
                    modifier = modifier,
                    selectedIndex = selectedIndex,
                    tabsList = listOf(
                        TabItem("Posts", painterResource(id = R.drawable.round_grid)),
                        TabItem("Profile", painterResource(id = R.drawable.baseline_account_box_24))
                    )
                ) {
                    selectedIndex = it
                }
                when (selectedIndex) {
                    0 -> PostSection(flow.posts, modifier = modifier.fillMaxWidth())
                    1 -> PostSection(flow.posts, modifier = modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun Highlights(highlightsList: List<HighlightItem>, modifier: Modifier = Modifier) {
    LazyRow(
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = rememberLazyListState()
    ) {
        items(highlightsList.size) {
            Spacer(modifier = modifier.width(15.dp))
            Column(modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                RoundImage(
                    image = highlightsList[it].highlightUrl,
                    modifier = modifier.size(70.dp)
                )
                Spacer(modifier = modifier.height(5.dp))
                Text(
                    text = highlightsList[it].title,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun TabView(
    modifier: Modifier,
    tabsList: List<TabItem>,
    selectedIndex: Int,
    onTabSelected: (selectedIndex: Int) -> Unit
) {
    val inactiveColor = Color.LightGray

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
                    onTabSelected(index)
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
}

@Composable
fun PostSection(
    posts: List<Post>,
    modifier: Modifier
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3),Modifier.scale(1.01f)) {
        items(posts.size) {
            Image(
                modifier = modifier
                    .padding((0.75).dp)
                    .aspectRatio(1f),
//                painter = painterResource(id = R.drawable.p),
                painter = rememberImagePainter(data = posts[it].postUrl) { error(R.drawable.instagram) },
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
fun TopAppBar(username: String, modifier: Modifier = Modifier, navController: NavController) {
    Row(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.outline_lock_24),
                contentDescription = null,
                modifier = modifier
                    .scale(0.8f),
                tint = Color.Black
            )
            Text(
                text = username,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                contentDescription = null,
                modifier = modifier
                    .scale(0.8f),
                tint = Color.Black
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.rounded_menu_24),
            contentDescription = null,
            modifier = modifier
                .scale(0.8f)
                .clickable {
                    Log.d("TAG", "TopAppBar: navigated")
                    navController.navigate(Screen.Settings)
                }
            ,
            tint = Color.Black
        )
    }
}

@Composable
fun UserInfo(flow: ProfileItem, modifier: Modifier = Modifier) {
    Row(
        modifier
            .wrapContentHeight()
            .padding(horizontal = 15.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        RoundImage(
            image = flow.profileImageUrl,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1.05f)
        )

        Row(
            modifier
                .weight(3f)
                .wrapContentHeight()
                .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = flow.postsCount,
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Posts",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = flow.followersCount,
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Followers",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = flow.followingCount,
                    color = Color.Black,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Following",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun Bio(bio: String, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Text(
            text = bio,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight(500),
        )
    }
}

@Composable
fun Options(modifier: Modifier = Modifier) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
    ) {
        // Create references for the three elements
        val (editProfile, shareProfile, addPerson) = createRefs()

        // Create a height reference that will be shared
        val heightRef = createGuidelineFromTop(0.5f)

        // Edit Profile Button
        Row(
            modifier = Modifier
                .constrainAs(editProfile) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.wrapContent  // This height will be used as reference
                    width = Dimension.percent(0.4f)  // Using percent instead of weight
                }
                .border(1.dp, TransGray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Edit Profile",
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )
        }

        // Share Profile Button
        Row(
            modifier = Modifier
                .constrainAs(shareProfile) {
                    start.linkTo(editProfile.end, margin = 5.dp)
                    top.linkTo(editProfile.top)
                    bottom.linkTo(editProfile.bottom)
                    height = Dimension.fillToConstraints  // Match edit profile height
                    width = Dimension.percent(0.4f)
                }
                .border(1.dp, TransGray, RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Share Profile",
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                modifier = Modifier
                    .padding(vertical = 5.dp)
            )
        }

        // Add Person Icon
        Row(
            modifier = Modifier
                .constrainAs(addPerson) {
                    start.linkTo(shareProfile.end, margin = 5.dp)
                    top.linkTo(editProfile.top)
                    bottom.linkTo(editProfile.bottom)
                    height = Dimension.fillToConstraints  // Match edit profile height
                    width = Dimension.percent(0.15f)
                }
                .border(1.dp, TransGray, RoundedCornerShape(4.dp)),
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
}

@Composable
fun Line() {
    Column(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .border(1.dp, TransGray, CutCornerShape(1.dp))
    ) {}
}

@Composable
fun RoundImage(image: String, modifier: Modifier) {
    Image(
//        painter = painterResource(id = R.drawable.p),
        painter = if (image.isEmpty()) painterResource(id = R.drawable.user) else rememberImagePainter(data = image) { error(R.drawable.user) },
        contentDescription = "menu",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)

    )
}
