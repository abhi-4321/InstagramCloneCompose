package com.example.instagramclone.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.model.StoryItem
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.LightGray
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.delay

//@Preview(showSystemUi = true)
@Composable
fun Story(
    modifier: Modifier = Modifier,
    userId: Int,
    profileImageUrl: String,
    fullName: String,
    username: String,
    viewModel: MainViewModel,
    onNavigateToNextUser: () -> Unit,
    onNavigateToPreviousUser: () -> Unit
) {

    LaunchedEffect(Unit) {
        viewModel.fetchStories(userId)
    }

    val storyListState = viewModel.storiesFlow.collectAsState()

    var storyListGP by remember {
        mutableStateOf(emptyList<StoryItem>())
    }

    var currentStoryIndex by remember { mutableIntStateOf(0) }
    var progress by remember { mutableFloatStateOf(0f) }
    var isPaused by remember { mutableStateOf(false) }
    var textMessageState by remember { mutableStateOf("") }

    // Story duration in milliseconds (5 seconds per story)
    val storyDuration = 5000L
    val updateInterval = 50L // Update every 50ms for smooth animation
    val progressIncrement = updateInterval.toFloat() / storyDuration

    LaunchedEffect(currentStoryIndex, isPaused, storyListState.value) {
        if (storyListState.value is MainViewModel.ApiResponse.Success<*> && !isPaused) {
            val storyList = (storyListState.value as MainViewModel.ApiResponse.Success<List<StoryItem>>).data!!

            storyListGP = storyList

            if (currentStoryIndex < storyList.size) {
                while (progress < 1f && !isPaused && currentStoryIndex < storyList.size) {
                    delay(updateInterval)
                    progress += progressIncrement
                }

                if (progress >= 1f) {
                    if (currentStoryIndex < storyList.size - 1) {
                        // Move to next story
                        currentStoryIndex++
                        progress = 0f
                    } else {
                        // All stories completed - navigate to next user
                        onNavigateToNextUser()
                    }
                }
            }
        }
    }

    fun goToNextStory() {
        if (currentStoryIndex < storyListGP.size - 1) {
            currentStoryIndex++
            progress = 0f
        } else {
            onNavigateToNextUser()
        }
    }

    // Function to go to previous story
    fun goToPreviousStory() {
        if (currentStoryIndex > 0) {
            currentStoryIndex--
            progress = 0f
        } else {
            // If on first story, restart it
            onNavigateToPreviousUser()
        }
    }

    when (storyListState.value) {
        is MainViewModel.ApiResponse.Failure -> {
            Box(
                modifier
                    .background(Color.Black)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxHeight()
            ) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier
                        .size(44.dp)
                        .align(Alignment.Center),
                    trackColor = Color.Transparent,
                    color = Color.LightGray,
                    strokeWidth = 2.dp,
                )

                Column(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    LinearProgressIndicator(
                        progress = { 0F },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .padding(horizontal = 5.dp),
                        strokeCap = StrokeCap.Round,
                        color = Color.White,
                        trackColor = LightGray
                    )

                    Row(
                        modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier.padding(horizontal = 12.dp)
                            ) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(42.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier.width(10.dp))

                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = username,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                }

                                Spacer(modifier.width(7.dp))

                                Text(
                                    text = "21h",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray,
                                    style = TextStyle.Default.copy(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 1f),
                                            blurRadius = 2f
                                        )
                                    ),
                                    modifier = modifier
                                        .padding(top = 5.dp)
                                        .align(Alignment.Top)
                                )
                            }

                        }
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                    }

                    Spacer(modifier.weight(1f))

                    Box(modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()) {
                        Box(
                            modifier = modifier
                                .padding(start = 5.dp, end = 5.dp, bottom = 65.dp)
                                .border(
                                    width = 1.dp,
                                    color = LightGray,
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .background(color = Color.Transparent)
                                .padding(horizontal = 20.dp, vertical = 12.dp) // Inner padding
                        ) {
                            BasicTextField(
                                value = textMessageState,
                                onValueChange = { textMessageState = it },
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start,
                                ),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (textMessageState.isEmpty()) {
                                        Text(
                                            "Search",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            style = TextStyle.Default.copy(
                                                shadow = Shadow(
                                                    color = Color.Black,
                                                    offset = Offset(0f, 1f),
                                                    blurRadius = 2f
                                                )
                                            )
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )
                        }

                        Row(modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 5.dp, top = 7.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.notliked),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )

                            Spacer(modifier.width(10.dp))

                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        MainViewModel.ApiResponse.Idle -> {}
        MainViewModel.ApiResponse.Loading -> {
            Box(
                modifier
                    .background(Color.Black)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxHeight()
            ) {
                CircularProgressIndicator(
                    modifier
                        .size(44.dp)
                        .align(Alignment.Center),
                    trackColor = Color.Transparent,
                    color = Color.LightGray,
                    strokeWidth = 2.dp,
                )

                Column(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    LinearProgressIndicator(
                        progress = { 0F },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .padding(horizontal = 5.dp),
                        strokeCap = StrokeCap.Round,
                        color = Color.White,
                        trackColor = LightGray
                    )

                    Row(
                        modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier.padding(horizontal = 12.dp)
                            ) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(42.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier.width(10.dp))

                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = username,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                }

                                Spacer(modifier.width(7.dp))

                                Text(
                                    text = "21h",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray,
                                    style = TextStyle.Default.copy(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 1f),
                                            blurRadius = 2f
                                        )
                                    ),
                                    modifier = modifier
                                        .padding(top = 5.dp)
                                        .align(Alignment.Top)
                                )
                            }

                        }
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                    }

                    Spacer(modifier.weight(1f))

                    Box(modifier
                        .padding(horizontal = 5.dp)
                        .fillMaxWidth()) {
                        Box(
                            modifier = modifier
                                .padding(start = 5.dp, end = 5.dp, bottom = 65.dp)
                                .border(
                                    width = 1.dp,
                                    color = LightGray,
                                    shape = RoundedCornerShape(28.dp)
                                )
                                .background(color = Color.Transparent)
                                .padding(horizontal = 20.dp, vertical = 12.dp) // Inner padding
                        ) {
                            BasicTextField(
                                value = textMessageState,
                                onValueChange = { textMessageState = it },
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start,
                                ),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (textMessageState.isEmpty()) {
                                        Text(
                                            "Search",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            style = TextStyle.Default.copy(
                                                shadow = Shadow(
                                                    color = Color.Black,
                                                    offset = Offset(0f, 1f),
                                                    blurRadius = 2f
                                                )
                                            )
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )
                        }

                        Row(modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 5.dp, top = 7.dp)) {
                            Icon(
                                painter = painterResource(R.drawable.notliked),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )

                            Spacer(modifier.width(10.dp))

                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        is MainViewModel.ApiResponse.Success<*> -> {

            val storyList =
                (storyListState.value as MainViewModel.ApiResponse.Success<List<StoryItem>>).data!!

            Box(
                modifier
                    .background(Color.Black)
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .fillMaxHeight()
            ) {
                // Story Image
                if (currentStoryIndex < storyList.size) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.88f),
                        model = storyList[currentStoryIndex].storyUrl, // Use current story
                        contentScale = ContentScale.Inside,
                        contentDescription = null
                    )
                }

                // Invisible tap areas for navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.88f)
                    /*.pointerInput(Unit){
                        detectTapGestures(
                            onLongPress = {
                                isPaused = true
                                tryAwaitRelease()
                                isPaused = false
                            }
                        )
                    }*/
                ) {
                    // Left tap area (previous story)
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { goToPreviousStory() }
                    )

                    // Right tap area (next story)
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { goToNextStory() }
                    )
                }

                Column(
                    modifier = modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    // Progress indicators
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp)
                    ) {
                        for (i in storyList.indices) {
                            val indicatorProgress = when {
                                i < currentStoryIndex -> 1f // Completed stories
                                i == currentStoryIndex -> progress // Current story
                                else -> 0f // Upcoming stories
                            }

                            LinearProgressIndicator(
                                progress = { indicatorProgress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(3.dp)
                                    .padding(horizontal = 2.dp),
                                strokeCap = StrokeCap.Round,
                                color = Color.White,
                                trackColor = Color.LightGray.copy(alpha = 0.3f)
                            )
                        }
                    }

                    Row(
                        modifier
                            .padding(top = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = modifier.padding(horizontal = 12.dp)
                            ) {
                                AsyncImage(
                                    model = profileImageUrl,
                                    contentDescription = "menu",
                                    contentScale = ContentScale.Crop,
                                    modifier = modifier
                                        .size(42.dp)
                                        .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                        .clip(CircleShape)
                                )

                                Spacer(modifier.width(10.dp))

                                Column(verticalArrangement = Arrangement.Center) {
                                    Text(
                                        text = fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = username,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White,
                                        style = TextStyle.Default.copy(
                                            shadow = Shadow(
                                                color = Color.Black,
                                                offset = Offset(0f, 1f),
                                                blurRadius = 2f
                                            )
                                        )
                                    )
                                }

                                Spacer(modifier.width(7.dp))

                                Text(
                                    text = "21h",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray,
                                    style = TextStyle.Default.copy(
                                        shadow = Shadow(
                                            color = Color.Black,
                                            offset = Offset(0f, 1f),
                                            blurRadius = 2f
                                        )
                                    ),
                                    modifier = modifier
                                        .padding(top = 5.dp)
                                        .align(Alignment.Top)
                                )
                            }

                        }
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.White)
                    }

                    Spacer(modifier.weight(1f))

                    Box(
                        modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = modifier
                                .padding(start = 5.dp, end = 5.dp, bottom = 65.dp)
                                .border(width = 1.dp, color = LightGray, shape = RoundedCornerShape(28.dp))
                                .background(color = Color.Transparent)
                                .padding(horizontal = 20.dp, vertical = 12.dp) // Inner padding
                        ) {
                            BasicTextField(
                                value = textMessageState,
                                onValueChange = { textMessageState = it },
                                textStyle = LocalTextStyle.current.copy(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal,
                                    textAlign = TextAlign.Start,
                                ),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (textMessageState.isEmpty()) {
                                        Text(
                                            "Search",
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Medium,
                                            style = TextStyle.Default.copy(
                                                shadow = Shadow(
                                                    color = Color.Black,
                                                    offset = Offset(0f, 1f),
                                                    blurRadius = 2f
                                                )
                                            )
                                        )
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )
                        }

                        Row(
                            modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 5.dp, top = 7.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.notliked),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )

                            Spacer(modifier.width(10.dp))

                            Icon(
                                painter = painterResource(R.drawable.share),
                                contentDescription = null,
                                modifier = modifier.size(26.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}