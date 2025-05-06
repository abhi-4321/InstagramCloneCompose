package com.example.instagramclone.screen.chat

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.model.ReceiverInfo
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.WhiteVar
import com.example.instagramclone.viewmodel.MainViewModel
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun Messages(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {

    var searchText by remember {
        mutableStateOf("")
    }

    val userDetailsState by viewModel.liveData.collectAsState()

    var username by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    LaunchedEffect(userDetailsState) {
        if (userDetailsState is MainViewModel.ApiResponse.Success) {
            imageUrl = (userDetailsState as MainViewModel.ApiResponse.Success).data!!.profileImageUrl
            username = (userDetailsState as MainViewModel.ApiResponse.Success).data!!.username
        }
    }

    val context = LocalContext.current

    val chatDisplayUsers by viewModel.chatUsersFlow.collectAsState()

    LaunchedEffect(Unit) {
        if (chatDisplayUsers !is MainViewModel.ApiResponse.Success<*>) {
            viewModel.fetchChatUsers()
        }
    }

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
                    painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
                    contentDescription = null,
                    modifier = modifier.size(28.dp),
                    tint = Color.Black
                )
                Spacer(modifier.width(10.dp))
                Text(
                    text = username,
                    fontSize = 21.sp,
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
                painter = painterResource(id = R.drawable.edit),
                contentDescription = null,
                modifier = modifier
                    .size(28.dp)
                    .clickable {
                        navController.navigate(Screen.NewMessage)
                    },
                tint = Color.Black
            )
        }

        LazyColumn {
            item {
                Spacer(modifier.height(10.dp))

                Box(
                    modifier = modifier
                        .padding(horizontal = 10.dp)
                        .background(color = WhiteVar, shape = RoundedCornerShape(28.dp))
                        .padding(horizontal = 20.dp, vertical = 12.dp) // Inner padding
                ) {
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                        ),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text(
                                    "Search",
                                    color = Color.Gray,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier.height(15.dp))

                Column(
                    modifier
                        .wrapContentSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier.wrapContentSize(), contentAlignment = Alignment.BottomEnd) {
                        AsyncImage(
                            model = if (imageUrl.isEmpty()) { R.drawable.user } else imageUrl,
                            contentDescription = "menu",
                            contentScale = ContentScale.Crop,
                            modifier = modifier
                                .size(70.dp)
                                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                .padding((1.5).dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = modifier.height(5.dp))
                    Text(
                        text = "Your note",
                        fontSize = 12.sp,
                    )
                }

                Row(
                    modifier
                        .padding(10.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Messages", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    Text(
                        "Requests",
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }

                Spacer(modifier.height(10.dp))
            }

            when (chatDisplayUsers) {
                is MainViewModel.ApiResponse.Failure -> {
                    Toast.makeText(
                        context,
                        "Failed to fetch the display users : ${(chatDisplayUsers as MainViewModel.ApiResponse.Failure).error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MainViewModel.ApiResponse.Idle -> {}
                is MainViewModel.ApiResponse.Success -> {
                    val list = (chatDisplayUsers as MainViewModel.ApiResponse.Success).data!!
                    val senderId = (userDetailsState as MainViewModel.ApiResponse.Success).data!!.id
                    items(list.size) {
                        Row(
                            modifier
                                .padding(horizontal = 10.dp)
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Screen.Chat(list[it].receiverId,list[it].profileImageUrl,list[it].username,list[it].fullName,senderId))
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = if (list[it].profileImageUrl.isEmpty()) R.drawable.user else list[it].profileImageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(55.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier.width(15.dp))

                                Column {
                                    Text(
                                        text = list[it].fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Spacer(modifier.height(3.dp))

                                    val zonedDateTime = ZonedDateTime.parse(list[it].lastChat.timestamp)
                                    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
                                    val formattedDate = zonedDateTime.format(formatter)

                                    val now = ZonedDateTime.now()
                                    val duration = Duration.between(zonedDateTime, now)

                                    val time = if (!duration.isNegative) {
                                        when {
                                            duration.toDays() >= 1 -> "${duration.toDays()}d"
                                            duration.toHours() >= 1 -> "${duration.toHours()}h"
                                            else -> "${duration.toMinutes()}m"
                                        }
                                    } else {
                                        formattedDate
                                    }

                                    Text(
                                        text = "${list[it].lastChat.chat} • $time",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Gray
                                    )
                                }

                            }
                            Icon(
                                painter = painterResource(id = R.drawable.outline_camera_alt_24),
                                contentDescription = null,
                                modifier = modifier.size(28.dp),
                                tint = Color.Gray
                            )
                        }

                        Spacer(modifier.height(12.dp))
                    }
                }

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
            }
        }
    }
}