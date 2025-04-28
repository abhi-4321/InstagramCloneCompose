package com.example.instagramclone.screen.chat

import android.util.Log
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instagramclone.R
import com.example.instagramclone.model.Chat
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.ui.theme.PinkDark
import com.example.instagramclone.ui.theme.Purple
import com.example.instagramclone.ui.theme.WhiteVar2
import com.example.instagramclone.viewmodel.ChatViewModel
import com.example.instagramclone.viewmodel.ChatViewModelFactory
import com.example.instagramclone.viewmodel.MainViewModel
import java.time.ZonedDateTime

@Composable
fun Chat(
    modifier: Modifier = Modifier,
    retrofitInterfaceMain: RetrofitInterfaceMain,
    receiverId: Int,
    senderId: Int
) {

    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(senderId, retrofitInterfaceMain)
    )

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current

    var textChatState by remember {
        mutableStateOf("")
    }

    val previousChats by chatViewModel.previousChats.collectAsState()

    LaunchedEffect(Unit) {
        if (previousChats !is MainViewModel.ApiResponse.Success<*>) {
            chatViewModel.fetchPreviousChats(receiverId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().statusBarsPadding()
    ) {
        // 1. Top Header - Always at top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp),
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

        // 2. Messages area - Takes all available space
        Box(
            modifier = Modifier // This is the key - fills available space between header and input
                .fillMaxWidth()
                .weight(1f)

        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
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
                        Log.d("Chats", list.toString())
                        items(list.size) { index ->
                            if (list[index].senderId == senderId) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .background(
                                                color = Purple,
                                                shape = RoundedCornerShape(28.dp)
                                            )
                                            .padding(
                                                horizontal = 15.dp,
                                                vertical = 10.dp
                                            ) // Inner padding
                                    ) {
                                        Text(
                                            text = list[index].chat,
                                            fontSize = 14.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
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
                                            .background(
                                                color = WhiteVar2,
                                                shape = RoundedCornerShape(28.dp)
                                            )
                                            .padding(
                                                horizontal = 15.dp,
                                                vertical = 10.dp
                                            ) // Inner padding
                                    ) {
                                        Text(
                                            text = list[index].chat,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
              /*  items(sampleChatMessages.size) { index ->
                    if (sampleChatMessages[index].senderId == senderId) {
                        Spacer(modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .background(
                                        color = Purple,
                                        shape = RoundedCornerShape(28.dp)
                                    )
                                    .padding(
                                        horizontal = 15.dp,
                                        vertical = 10.dp
                                    ) // Inner padding
                            ) {
                                Text(
                                    text = sampleChatMessages[index].chat,
                                    fontSize = 14.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
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
                                    .background(
                                        color = WhiteVar2,
                                        shape = RoundedCornerShape(28.dp)
                                    )
                                    .padding(
                                        horizontal = 15.dp,
                                        vertical = 10.dp
                                    ) // Inner padding
                            ) {
                                Text(
                                    text = sampleChatMessages[index].chat,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }*/
            }
        }

        // 3. Input area - Always at bottom

        Column(modifier.fillMaxWidth().imePadding()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = WhiteVar2, shape = RoundedCornerShape(28.dp))
                    .padding(horizontal = 5.dp, vertical = 5.dp)
                , // Inner padding
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
                        onValueChange = { textChatState = it },
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
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp, end = 55.dp)
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
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
                                val list = listOf(senderId, receiverId).sorted()
                                val id = list.joinToString( "_" )
                                chatViewModel.sendMessage(
                                    Chat(
                                        id = id,
                                        senderId = senderId,
                                        receiverId = receiverId,
                                        chat = textChatState,
                                        attachment = "",
                                        timestamp = (ZonedDateTime.now()).toString(),
                                        participants = list
                                    )
                                )
                                textChatState = ""
                                focusManager.clearFocus()
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

        Spacer(modifier.height(15.dp))
    }
}