package com.example.instagramclone.screen.main

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.WhiteVar2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Create(modifier: Modifier = Modifier, navController: NavController = rememberNavController(), onCreate: () -> List<Uri>) {

    val list = onCreate()

    var selectedUri by remember { mutableStateOf("") }

    Column(
        modifier
            .statusBarsPadding()
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigateUp()
                        },
                    tint = Color.Black
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text("New post", fontWeight = FontWeight.Bold, fontSize = 21.sp)
            }
            Text("Next", color = Color.Blue, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = modifier.clickable {
                navController.navigate(Screen.EditPost(selectedUri))
            })
        }

        LazyColumn {
            // Full-width image
            item {
                Spacer(modifier.height(10.dp))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    painter = rememberImagePainter(data = if (list.isNotEmpty()) list[0] else ""),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                selectedUri = if (list.isNotEmpty()) list[0].toString() else ""
            }

            // Sticky header
            stickyHeader {
                Row(
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Recents",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                            contentDescription = null,
                            modifier = modifier
                                .scale(0.8f),
                            tint = Color.Black
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .background(
                                    color = WhiteVar2,
                                    shape = RoundedCornerShape(24.dp)
                                ).padding(horizontal = 10.dp, vertical = 5.dp)
                        // Inner padding
                        ) {
                            Spacer(modifier.width(3.dp))
                            Icon(
                                painter = painterResource(R.drawable.baseline_filter_none_24),
                                contentDescription = null,
                                modifier = modifier.size(16.dp)
                            )

                            Spacer(modifier.width(5.dp))

                            Text(
                                text = "Select multiple",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                            )
                        }

                        Spacer(modifier.width(5.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    color = WhiteVar2,
                                    shape = RoundedCornerShape(24.dp)
                                ).padding(5.dp)
                        // Inner padding
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.outline_camera_alt_24),
                                contentDescription = null,
                                modifier = modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier.height(2.dp))

            }

            // Grid items inside a LazyVerticalGrid
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .scale(1.01f), // Set height to make it scrollable
                    userScrollEnabled = false // Prevent nested scroll conflict
                ) {
                    items(list.size) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding((0.75).dp)
                                .aspectRatio(1f),
                            painter = rememberImagePainter(data = list[it]),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}