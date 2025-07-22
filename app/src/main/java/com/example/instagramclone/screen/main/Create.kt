package com.example.instagramclone.screen.main

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.WhiteVar2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Create(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onCreate: suspend (offset: Int) -> List<Pair<Uri, Bitmap?>>
) {

    // State for pagination
    val loadedImages = remember { mutableStateListOf<Pair<Uri,Bitmap?>>() }
    var firstImageLoaded by remember { mutableStateOf(false) }
    var currentPage by remember { mutableIntStateOf(0) }
    val imagesPerPage = 12
    var isLoading by remember { mutableStateOf(false) }
    var hasMoreImages by remember { mutableStateOf(true) }
    var selectedUri by remember { mutableStateOf("") }

    // Scroll state for infinite scrolling detection
    val listState = rememberLazyListState()

    // First load - get the first batch of images and the first image
    LaunchedEffect(Unit) {
        val initialImages = onCreate(0)

        loadedImages.addAll(initialImages)

        Log.d("CreateCompose", "Initial Images : ${initialImages.size}")

        if (initialImages.isNotEmpty()) {
            selectedUri = initialImages[0].first.toString()
            firstImageLoaded = true
        }

        // If we got fewer images than requested, we've loaded all images
        if (initialImages.size < imagesPerPage) {
            hasMoreImages = false
        }
    }

    // Detect when we need to load more images
    val shouldLoadMore by remember {
        derivedStateOf {
            if (!isLoading && hasMoreImages) {
                val layoutInfo = listState.layoutInfo
                val totalItemsNumber = layoutInfo.totalItemsCount
                val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

                // Load more when we're 3 items from the end
                lastVisibleItemIndex > totalItemsNumber - 2
            } else {
                false
            }
        }
    }

    // Load more images when needed
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !isLoading) {
            isLoading = true
            currentPage++

            val nextOffset = currentPage * imagesPerPage
            val nextImages = onCreate(nextOffset)


            loadedImages.addAll(nextImages)

            // If we got fewer images than requested, we've loaded all images
            if (nextImages.size < imagesPerPage) {
                hasMoreImages = false
            }

            isLoading = false
        }
    }

    Column(
        modifier
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .fillMaxHeight()
    ) {
        // Top bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(color = Color.White)
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
            Text(
                text = "Next",
                color = Color.Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = modifier
                    .clickable {
                        if (selectedUri.isNotEmpty()) {
                            navController.navigate(Screen.EditPost(selectedUri))
                        }
                    }
            )
        }

        // Content area with LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            // Full-width image
            item {
                Spacer(modifier = Modifier.height(10.dp))
                if (firstImageLoaded && loadedImages.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberImagePainter(data = selectedUri.toUri()),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White)
                        } else {
                            Text("No images found", color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            // Sticky header
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                modifier = Modifier.scale(0.8f),
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
                                    )
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Spacer(modifier = Modifier.width(3.dp))
                                Icon(
                                    painter = painterResource(R.drawable.baseline_filter_none_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))

                                Text(
                                    text = "Select multiple",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                )
                            }

                            Spacer(modifier = Modifier.width(5.dp))

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = WhiteVar2,
                                        shape = RoundedCornerShape(24.dp)
                                    )
                                    .padding(5.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_camera_alt_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Grid items with pagination
            val chunkedImages = loadedImages.chunked(4)
            items(chunkedImages.size) { rowIndex ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.75.dp)
                ) {
                    chunkedImages[rowIndex].forEach { pair ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(0.75.dp)
                                .clickable {
                                    selectedUri = pair.first.toString()
                                }
                                .aspectRatio(1f)
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = rememberImagePainter(data = pair.second),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                            // Highlight selected image
                            if (pair.first.toString() == selectedUri) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x33D7D7D7))
                                )
                            }
                            /*
                            // Make each grid image clickable
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable {

                                    }
                            )*/
                        }
                    }

                    // Fill empty spaces in the last row if needed
                    val emptySpaces = 4 - chunkedImages[rowIndex].size
                    repeat(emptySpaces) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Loading indicator at the bottom
            if (isLoading || hasMoreImages) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.Gray,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CreatePreview() {
    Create(
        navController = rememberNavController()
    ) {
        offset ->
        emptyList()
    }
}