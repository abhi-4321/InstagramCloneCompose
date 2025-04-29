package com.example.instagramclone.screen.chat

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.instagramclone.R
import com.example.instagramclone.model.ReceiverInfo
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.viewmodel.MainViewModel

@Composable
fun NewMessage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {

    var textSearchState by remember {
        mutableStateOf("")
    }

    val allChatUsers by viewModel.allChatUsersFlow.collectAsState()
    val userDetailsState by viewModel.liveData.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (allChatUsers !is MainViewModel.ApiResponse.Success<*>) {
            viewModel.fetchAllChatUsers()
        }
    }

    Column(
        modifier
            .statusBarsPadding()
            .fillMaxHeight()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
                contentDescription = null,
                modifier = Modifier.size(30.dp).clickable { 
                    navController.navigateUp()
                },
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(20.dp))

            Text("New message", fontWeight = FontWeight.Bold, fontSize = 21.sp)
        }

        LazyColumn {
            item {
                Spacer(modifier.height(20.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("To:", fontSize = 18.sp, fontWeight = FontWeight.Normal, color = Gray)
                    Spacer(modifier = Modifier.width(20.dp))
                    BasicTextField(
                        value = textSearchState,
                        onValueChange = { textSearchState = it },
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Start,
                        ),
                        maxLines = 3,
                        decorationBox = { innerTextField ->
                            if (textSearchState.isEmpty()) {
                                Text(
                                    "Search",
                                    color = Gray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier.height(20.dp))

                Text(
                    "Suggested",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier.height(20.dp))
            }

            when (allChatUsers) {
                is MainViewModel.ApiResponse.Failure -> {
                    Toast.makeText(
                        context,
                        "Failed to fetch the display users : ${(allChatUsers as MainViewModel.ApiResponse.Failure).error}",
                        Toast.LENGTH_SHORT
                    ).show()
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

                is MainViewModel.ApiResponse.Success -> {
                    val list = (allChatUsers as MainViewModel.ApiResponse.Success).data!!
                    val senderId = (userDetailsState as MainViewModel.ApiResponse.Success).data!!.id
                    items(list.size) {
                        Row(
                            modifier
                                .padding(horizontal = 10.dp)
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.Chat(
                                            list[it].receiverId,list[it].profileImageUrl,list[it].username,list[it].fullName,
                                            senderId
                                        )
                                    )
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier.wrapContentSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
//                                    painter = painterResource(id = R.drawable.p),
                                    painter = rememberImagePainter(data = list[it].profileImageUrl) { R.drawable.user },
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
//                                        text = "Mahika Nandal",
                                        text = list[it].fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Black
                                    )
                                    Spacer(modifier.height(3.dp))

                                    Text(
//                                        text = "notmahika_",
                                        text = list[it].username,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        Spacer(modifier.height(10.dp))
                    }
                }
            }
        }
    }
}