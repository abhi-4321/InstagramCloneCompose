package com.example.instagramclone.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.model.Comment
import com.example.instagramclone.model.Post
import com.example.instagramclone.model.StoryDisplayUser
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.ui.theme.Pink
import com.example.instagramclone.ui.theme.fontFamily

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp")
@Composable
fun Home(
    modifier: Modifier = Modifier,
//         navController: NavController,
//         viewModel: MainViewModel
) {
    val context = LocalContext.current

    val displayList = listOf<StoryDisplayUser>(
        StoryDisplayUser(1, "_d_evil_02", ""),
        StoryDisplayUser(2, "_d_evil_03", ""),
        StoryDisplayUser(3, "zufu.kid", ""),
        StoryDisplayUser(4, "notmahika", ""),
        StoryDisplayUser(5, "piri", ""),
    )

    val postsList = listOf<Post>(
        Post(
            1,
            1,
            "",
            "Breeze in the sun",
            "24",
            listOf(2, 3, 4),
            "10",
            listOf(Comment(1, 2, 1, "Very good", "20", listOf(2, 3, 4)))
        ),
        Post(
            2,
            1,
            "",
            "Laying on the moon",
            "24",
            listOf(2, 3, 4),
            "10",
            listOf(Comment(1, 2, 1, "Very good", "20", listOf(2, 3, 4)))
        ),
        Post(
            3,
            2,
            "",
            "Living on the earth",
            "24",
            listOf(2, 3, 4),
            "10",
            listOf(Comment(1, 2, 1, "Very good", "20", listOf(2, 3, 4)))
        ),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .statusBarsPadding()
    ) {
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
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
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
            items(displayList.size) {
                Spacer(modifier = modifier.width(15.dp))
                Column(
                    modifier.wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RoundImage(
                        image = displayList[it].profileImageUrl,
                        modifier = modifier.size(70.dp)
                    )
                    Spacer(modifier = modifier.height(5.dp))
                    Text(
                        text = displayList[it].username,
                        fontSize = 13.sp,
                        letterSpacing = TextUnit(-0.2f, TextUnitType.Sp)
                    )
                }
            }
        }

        Spacer(modifier.height(15.dp))
        Line()

        LazyColumn(
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = rememberLazyListState()
        ) {
            items(displayList.size) {
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
                            painter = painterResource(id = R.drawable.p),
                            //      painter = rememberImagePainter(data = image) { error(R.drawable.p) },
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
                            text = displayList[it].username,
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
                    painter = painterResource(id = R.drawable.p),
                    //       painter = rememberImagePainter(data = image) { error(R.drawable.p) },
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
                        Text("1139", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier.width(10.dp))
                        Icon(
                            modifier = modifier.fillMaxHeight().offset(y = (-0.5).dp),
                            painter = painterResource(id = R.drawable.comment),
                            tint = Color.Unspecified,
                            contentDescription = null
                        )
                        Spacer(modifier.width(3.dp))
                        Text("58", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
                    "Liked by zufu.kid and 1138 others",
                    modifier = modifier.padding(horizontal = 15.dp),
                    fontSize = 14.sp
                )

                Spacer(modifier.height(5.dp))
                Text(
                    "_d_evil_02 On the first floor with a cameraman and people be watching from below like i am doing something wrong but i don't care!!!",
                    modifier = modifier.padding(horizontal = 15.dp),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier.height(5.dp))
                Text(
                    "View all 58 comments",
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