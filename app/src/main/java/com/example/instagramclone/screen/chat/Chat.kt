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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.model.Chat
import com.example.instagramclone.ui.theme.PinkDark
import com.example.instagramclone.ui.theme.Purple
import com.example.instagramclone.ui.theme.WhiteVar
import com.example.instagramclone.viewmodel.ChatViewModel
import com.example.instagramclone.viewmodel.MainViewModel
import java.time.ZonedDateTime

@Composable
fun Chat(modifier: Modifier = Modifier, viewModel: ChatViewModel, mainViewModel: MainViewModel, receiverId: Int) {
    // Your sample data and state remain the same
    /*val sampleChatMessages = listOf(
        Chat(
            id = "msg1",
            senderId = 101,
            receiverId = 202,
            chat = "Hey, how are you doing today?",
            timestamp = "2025-04-24T09:15:23",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg2",
            senderId = 202,
            receiverId = 101,
            chat = "I'm good! Just finished a meeting. How about you?",
            timestamp = "2025-04-24T09:16:45",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg3",
            senderId = 101,
            receiverId = 202,
            chat = "Working on that new app design we discussed yesterday",
            timestamp = "2025-04-24T09:18:12",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg4",
            senderId = 202,
            receiverId = 101,
            chat = "Sounds great! Can you share a preview when you're done?",
            timestamp = "2025-04-24T09:20:05",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg5",
            senderId = 101,
            receiverId = 202,
            chat = "Sure thing! I should have something ready by this evening.",
            attachment = "design_sketch.jpg",
            timestamp = "2025-04-24T09:21:33",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg6",
            senderId = 202,
            receiverId = 101,
            chat = "Perfect! By the way, are we still meeting for lunch tomorrow?",
            timestamp = "2025-04-24T09:25:18",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg7",
            senderId = 101,
            receiverId = 202,
            chat = "Yes, definitely! The usual place at 1 PM?",
            timestamp = "2025-04-24T09:28:47",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg8",
            senderId = 202,
            receiverId = 101,
            chat = "Sounds good to me. I'll bring those documents we talked about.",
            timestamp = "2025-04-24T09:30:22",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg9",
            senderId = 202,
            receiverId = 101,
            chat = "Awesome! Oh, I almost forgot to ask - did you check out that library I sent you?",
            timestamp = "2025-04-24T09:35:12",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg10",
            senderId = 202,
            receiverId = 101,
            chat = "Yes! It's exactly what we needed for the project. Great find!",
            timestamp = "2025-04-24T09:38:59",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg9",
            senderId = 101,
            receiverId = 202,
            chat = "Awesome! Oh, I almost forgot to ask - did you check out that library I sent you?",
            timestamp = "2025-04-24T09:35:12",
            participants = listOf(101, 202)
        ),
        Chat(
            id = "msg10",
            senderId = 202,
            receiverId = 101,
            chat = "Yes! It's exactly what we needed for the project. Great find!",
            timestamp = "2025-04-24T09:38:59",
            participants = listOf(101, 202)
        )
    )*/

    var textChatState by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val previousChats by mainViewModel.previousChats.collectAsState()

    LaunchedEffect(Unit) {
        if (previousChats !is MainViewModel.ApiResponse.Success<*>) {
            mainViewModel.fetchPreviousChats()
        }
    }

    val messages by viewModel.message.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        // Main content layout with fixed positioning
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 1. Top Header - Always at top
            ChatHeader()

            // 2. Messages area - Takes all available space
            Box(
                modifier = Modifier
                    .weight(1f) // This is the key - fills available space between header and input
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp),
                    reverseLayout = false // Set to true if you want newest messages at bottom
                ) {
                    when (previousChats) {
                        is MainViewModel.ApiResponse.Failure -> {
                            Toast.makeText(
                                context,
                                "Failed to fetch the feed : ${(previousChats as MainViewModel.ApiResponse.Failure).error}",
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
                            val list = (previousChats as MainViewModel.ApiResponse.Success).data!!
                            items(list.size) { index ->
                                MessageItem(message = list[index])
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }

            // 3. Input area - Always at bottom
            ChatInputArea(
                textChatState = textChatState,
                onTextChange = { textChatState = it },
                viewModel = viewModel,
                receiverId = receiverId
            )
        }
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(15.dp))

            Image(
                painter = painterResource(id = R.drawable.p),
                contentDescription = "menu",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .padding((1.5).dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Text(
                    text = "Abhinav Mahalwal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "_d_evil_02",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.DarkGray
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.call),
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(20.dp))
            Icon(
                painter = painterResource(id = R.drawable.video),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun MessageItem(message: Chat) {
    if (message.senderId == 101) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .background(color = Purple, shape = RoundedCornerShape(28.dp))
                    .padding(horizontal = 15.dp, vertical = 10.dp) // Inner padding
            ) {
                Text(
                    text = message.chat,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                )
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Image(
                painter = painterResource(id = R.drawable.p),
                contentDescription = "menu",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 10.dp, start = 10.dp)
                    .size(35.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .padding((1.5).dp)
                    .clip(CircleShape)
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .background(color = WhiteVar, shape = RoundedCornerShape(28.dp))
                    .padding(horizontal = 15.dp, vertical = 10.dp) // Inner padding
            ) {
                Text(
                    text = message.chat,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                )
            }
        }
    }
}

@Composable
fun ChatInputArea(textChatState: String, onTextChange: (String) -> Unit, viewModel: ChatViewModel, receiverId: Int) {
    Box(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(color = WhiteVar, shape = RoundedCornerShape(28.dp))
            .padding(horizontal = 5.dp, vertical = 5.dp), // Inner padding
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(color = PinkDark, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera",
                    modifier = Modifier.size(22.dp)
                )
            }

            BasicTextField(
                value = textChatState,
                onValueChange = onTextChange,
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                ),
                maxLines = 3,
                decorationBox = { innerTextField ->
                    if (textChatState.isEmpty()) {
                        Text(
                            "Message...",
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .padding(start = 10.dp, end = 55.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState(), true)
            )
        }

        if (textChatState.isEmpty()) {
            Row {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = null,
                    modifier = Modifier.size(23.dp)
                )
                Spacer(modifier = Modifier.width(9.dp))
                Icon(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .offset(y = (-1).dp)
                )
                Spacer(modifier = Modifier.width(9.dp))
                Icon(
                    painter = painterResource(id = R.drawable.sticker),
                    contentDescription = null,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(9.dp))
                Icon(
                    painter = painterResource(id = R.drawable.add_circle),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 9.dp)
                        .size(23.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .height(32.dp)
                    .width(45.dp)
                    .background(color = Purple, shape = RoundedCornerShape(14.dp))
                    .clickable {
                        val list = listOf(1,receiverId)
                        val id = list.sorted().joinToString { "_" }
                        viewModel.sendMessage(
                            Chat(
                                id = id,
                                senderId = 1,
                                receiverId = receiverId,
                                chat = textChatState,
                                attachment = "",
                                timestamp = (ZonedDateTime.now()).toString(),
                                participants = listOf(1, 2)
                            )
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.share),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}