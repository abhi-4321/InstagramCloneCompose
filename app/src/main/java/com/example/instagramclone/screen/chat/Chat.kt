package com.example.instagramclone.screen.chat

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.model.Chat
import com.example.instagramclone.model.MessageGroup
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.network.main.RetrofitInterfaceMain
import com.example.instagramclone.ui.theme.PinkDark
import com.example.instagramclone.ui.theme.Purple
import com.example.instagramclone.ui.theme.WhiteVar2
import com.example.instagramclone.viewmodel.ChatViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun Chat(
    modifier: Modifier = Modifier,
    retrofitInterfaceMain: RetrofitInterfaceMain,
    mainViewModel: MainViewModel,
    receiverId: Int,
    profileImageUrl: String,
    username: String,
    fullName: String,
    senderId: Int,
    token: String
) {

    val chatViewModel: ChatViewModel = getViewModel {
        parametersOf(senderId,token)
    }

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

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
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize()
            .padding(top = 5.dp)
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

                AsyncImage(
                    model = if (profileImageUrl.isEmpty()) {
                        R.drawable.user
                    } else profileImageUrl,
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
                        text = fullName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = username,
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
                state = listState,
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
                                    .height(30.dp),
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
                        item {
                            Spacer(modifier.height(15.dp))
                            Column(
                                modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AsyncImage(
                                    model = if (profileImageUrl.isEmpty()) {
                                        R.drawable.user
                                    } else profileImageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .padding(vertical = 10.dp)
                                        .size(120.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .padding((1.5).dp)
                                        .clip(CircleShape)
                                )
                                Text(
                                    text = fullName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier.height(3.dp))
                                Text(
                                    text = username,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Spacer(modifier.height(10.dp))
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = WhiteVar2,
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                ) {
                                    Text(
                                        text = "View Profile",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                            Spacer(modifier.height(30.dp))
                        }

                        val list = (previousChats as MainViewModel.ApiResponse.Success).data!!
                        val groupedMessages = groupConsecutiveMessagesWithTimestamps(list)

                        Log.d("Chats", list.toString())

                        if (list.isNotEmpty()) {
                            coroutineScope.launch {
                                listState.animateScrollToItem(list.size - 1)
                            }
                        }

                        groupedMessages.forEachIndexed { groupIndex: Int, messageGroup: MessageGroup ->
                            val group = messageGroup.messages
                            val isFromCurrentUser = group.first().senderId == senderId

                            // Only display time if this group should show it
                            if (messageGroup.shouldDisplayTime) {
                                item {
                                    Spacer(modifier.height(15.dp))
                                    val time = formatTimestamp(timestamp = group.first().timestamp)

                                    Row(
                                        modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            time,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                    }
                                    Spacer(modifier.height(35.dp))

                                }
                            }

                            items(group.size) { index ->
                                val message = group[index]
                                val isFirstInGroup = index == 0
                                val isLastInGroup = index == group.size - 1

                                ChatBubble(
                                    message,
                                    isFromCurrentUser,
                                    isFirstInGroup,
                                    isLastInGroup,
                                    isLastInGroup && !isFromCurrentUser,
                                    profileImageUrl
                                )

                                if (!isLastInGroup) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                            }

                            item { Spacer(modifier = Modifier.height(12.dp)) }
                        }
                    }
                }
            }
        }

        // 3. Input area - Always at bottom

        Column(
            modifier
                .fillMaxWidth()
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(color = WhiteVar2, shape = RoundedCornerShape(28.dp))
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
                            .clickable(
                                indication = null,
                                interactionSource = null
                            ) {
                                val list = listOf(senderId, receiverId).sorted()
                                val id = list.joinToString("_")
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

                                if (previousChats is MainViewModel.ApiResponse.Success) {
                                    val list =
                                        (previousChats as MainViewModel.ApiResponse.Success).data!!
                                    if (list.isNotEmpty()) {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(list.size - 1)
                                        }
                                    }
                                }
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

private fun groupConsecutiveMessagesWithTimestamps(
    messages: List<Chat>
): List<MessageGroup> {
    val groupedMessages = mutableListOf<MessageGroup>()

    if (messages.isEmpty()) return groupedMessages

    // Force timestamp on the very first message
    var forceFirstMessageTimestamp = true

    var currentGroup = mutableListOf<Chat>()
    var currentSenderId = messages.firstOrNull()?.senderId ?: 0
    var lastMessageTime: ZonedDateTime? = null

    messages.forEachIndexed { index, message ->
        val messageTime = if (!message.timestamp.isNullOrEmpty()) {
            ZonedDateTime.parse(message.timestamp).withZoneSameInstant(ZoneId.systemDefault())
        } else null

        // For the first message, initialize the group
        if (index == 0) {
            currentGroup.add(message)
            lastMessageTime = messageTime
            return@forEachIndexed
        }

        val timeDifference = if (lastMessageTime != null && messageTime != null) {
            Duration.between(lastMessageTime, messageTime).toMinutes().absoluteValue
        } else 0

        // Start a new group if:
        // 1. Sender ID changes OR
        // 2. Time difference is more than 30 minutes (1/2 hour)
        if (currentSenderId != message.senderId || timeDifference >= 30) {
            // Add current group to the grouped messages list
            if (currentGroup.isNotEmpty()) {
                // Always show timestamp for first group
                val shouldDisplayTime = forceFirstMessageTimestamp || (timeDifference >= 30)
                groupedMessages.add(MessageGroup(currentGroup.toList(), shouldDisplayTime))
                forceFirstMessageTimestamp = false
            }

            // Start a new group
            currentGroup = mutableListOf(message)
            currentSenderId = message.senderId
        } else {
            // Continue the current group
            currentGroup.add(message)
        }

        // Update last message time
        lastMessageTime = messageTime
    }

    // Add the last group if not empty
    if (currentGroup.isNotEmpty()) {
        // If this is the only group (i.e., first messages), force timestamp
        val shouldDisplayTime = forceFirstMessageTimestamp || groupedMessages.isEmpty()
        groupedMessages.add(MessageGroup(currentGroup.toList(), shouldDisplayTime))
    }

    // Final safety check: force the very first group to show timestamp
    if (groupedMessages.isNotEmpty()) {
        val firstGroup = groupedMessages.first()
        if (!firstGroup.shouldDisplayTime) {
            groupedMessages[0] = MessageGroup(firstGroup.messages, true)
        }
    }

    return groupedMessages
}

fun formatTimestamp(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return ""

    val inputTime = ZonedDateTime.parse(timestamp).withZoneSameInstant(ZoneId.systemDefault())
    val now = ZonedDateTime.now(ZoneId.systemDefault())

    val duration = Duration.between(inputTime, now)
    val daysBetween = ChronoUnit.DAYS.between(inputTime.toLocalDate(), now.toLocalDate())
    val yearsBetween = ChronoUnit.YEARS.between(inputTime.toLocalDate(), now.toLocalDate())

    return when {
        duration.toHours() < 24 -> {
            "Today" + inputTime.format(DateTimeFormatter.ofPattern(" h:mm a", Locale.getDefault()))
        }

        daysBetween < 7 -> inputTime.dayOfWeek.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        ) + inputTime.format(DateTimeFormatter.ofPattern(" h:mm a", Locale.getDefault())) // "Mon"
        yearsBetween < 1 -> inputTime.format(
            DateTimeFormatter.ofPattern(
                "MMM d, h:mm a",
                Locale.getDefault()
            )
        ) // "Apr 2, 10:50 AM"
        else -> inputTime.format(
            DateTimeFormatter.ofPattern(
                "MMM d, yyyy",
                Locale.getDefault()
            )
        ) // "Apr 12, 2021"
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeableMessageItem(
    message: Chat,
    isFromCurrentUser: Boolean,
    isFirstInGroup: Boolean,
    isLastInGroup: Boolean,
    showProfileImage: Boolean,
    onReplySwipe: () -> Unit,
    onTimeSwipe: () -> Unit
) {
    // Setup swipe logic with gestures
    val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }
    val minSwipe = 80.dp
    val swipeableState = rememberSwipeableState(initialValue = 0)

    // Values for swipe threshold
    val replyThreshold = with(LocalDensity.current) { minSwipe.toPx() }
    val timeThreshold = with(LocalDensity.current) { -minSwipe.toPx() }

    // For manual gesture handling
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            // Check if swipe is enough to trigger action
                            when {
                                offsetX.value >= replyThreshold -> {
                                    onReplySwipe()
                                    // Animate back
                                    offsetX.animateTo(0f)
                                }

                                offsetX.value <= timeThreshold -> {
                                    onTimeSwipe()
                                    // Animate back
                                    offsetX.animateTo(0f)
                                }

                                else -> {
                                    // Not enough, animate back to original position
                                    offsetX.animateTo(0f)
                                }
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            offsetX.animateTo(0f)
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            // Limit dragging direction based on sender
                            val canDragRight =
                                !isFromCurrentUser // Only other user's messages can be replied to
                            val canDragLeft = true // All messages can show time

                            val newValue = offsetX.value + dragAmount

                            // Apply constraints based on sender
                            when {
                                newValue > 0 && !canDragRight -> return@launch
                                newValue < 0 && !canDragLeft -> return@launch
                                else -> offsetX.snapTo(newValue)
                            }
                        }
                    }
                )
            }
    ) {
        // Reply indicator
        if (offsetX.value > 0 && !isFromCurrentUser) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp)
                    .alpha(min(1f, offsetX.value / replyThreshold))
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reply",
                    tint = Color.Gray
                )
            }
        }

        // Time indicator
        if (offsetX.value < 0) {
            Box(
                modifier = Modifier
                    .align(if (isFromCurrentUser) Alignment.CenterStart else Alignment.CenterEnd)
                    .padding(horizontal = 16.dp)
                    .alpha(min(1f, -offsetX.value / timeThreshold))
            ) {
                Text(
                    text = "12:30 PM", // You would use a formatted time from the message
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        // The actual message bubble
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .fillMaxWidth()
        ) {
            ChatBubble(
                message = message,
                isFromCurrentUser = isFromCurrentUser,
                isFirstInGroup = isFirstInGroup,
                isLastInGroup = isLastInGroup,
                showProfileImage = showProfileImage,
                profileImageUrl = ""
            )
        }
    }
}

@Composable
fun ChatBubble(
    message: Chat,
    isFromCurrentUser: Boolean,
    isFirstInGroup: Boolean,
    isLastInGroup: Boolean,
    showProfileImage: Boolean,
    profileImageUrl: String
) {
    // Define the corner radius based on position in group
    val cornerRadius = when {
        // For single message in a group
        isFirstInGroup && isLastInGroup -> RoundedCornerShape(24.dp)

        // For first message in a group
        isFirstInGroup -> if (isFromCurrentUser) {
            RoundedCornerShape(16.dp, 16.dp, 5.dp, 16.dp)
        } else {
            RoundedCornerShape(16.dp, 16.dp, 16.dp, 5.dp)
        }

        // For last message in a group
        isLastInGroup -> if (isFromCurrentUser) {
            RoundedCornerShape(16.dp, 5.dp, 16.dp, 16.dp)
        } else {
            RoundedCornerShape(5.dp, 16.dp, 16.dp, 16.dp)
        }

        // For middle messages in a group
        else -> if (isFromCurrentUser) {
            RoundedCornerShape(16.dp, 5.dp, 5.dp, 16.dp)
        } else {
            RoundedCornerShape(5.dp, 16.dp, 16.dp, 5.dp)
        }
    }

    if (isFromCurrentUser) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .background(
                        color = Purple,
                        shape = cornerRadius
                    )
                    .padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    ) // Inner padding
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            if (showProfileImage) {
                AsyncImage(
                    model = if (profileImageUrl.isEmpty()) {
                        R.drawable.user
                    } else profileImageUrl,
                    contentDescription = "menu",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(top = 10.dp, start = 10.dp)
                        .size(35.dp)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                        .padding((1.5).dp)
                        .clip(CircleShape)
                )
            } else {
                Spacer(Modifier.width(45.dp))
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .background(
                        color = WhiteVar2,
                        shape = cornerRadius
                    )
                    .padding(
                        horizontal = 15.dp,
                        vertical = 10.dp
                    ) // Inner padding
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

@Preview(showSystemUi = true)
@Composable
fun ChatPreview() {
    Chat(
        retrofitInterfaceMain = RetrofitInstanceMain.getApiService(""),
        mainViewModel =  MainViewModel(RetrofitInstanceMain.getApiService("")),
        receiverId =  1,
        profileImageUrl =  "",
        username =  "Abhi",
        fullName =  "Abhinav",
        senderId =  2,
        token = ""
    )
}
