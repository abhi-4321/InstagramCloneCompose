package com.example.instagramclone.screen.main

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.main.RetrofitInstanceMain
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.WhiteGray
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun Search(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel) {

    var query by remember {
        mutableStateOf("")
    }

    var isActive by remember {
        mutableStateOf(false)
    }

    val debouncePeriod = 500L // milliseconds

    LaunchedEffect(Unit) {
        viewModel.fetchExplore()
    }

    LaunchedEffect(query) {
        snapshotFlow { query }
            .debounce(debouncePeriod)
            .filter { it.length >= 2 } // avoid short queries
            .distinctUntilChanged()
            .collect {
                viewModel.searchUsers(it)
            }
    }

    val searchResponse by viewModel.searchResults.collectAsState()
    val exploreState by viewModel.flowExplore.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isActive) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(22.dp)
                )
            }

            SearchBar(
                query,
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                onClickFocus = { isActive = !isActive }
            ) { query = it }
        }

        if (isActive) {

            when (searchResponse) {
                is MainViewModel.ApiResponse.Failure -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(
                            text = "An error occurred"
                        )
                    }
                }

                is MainViewModel.ApiResponse.Idle -> {}
                is MainViewModel.ApiResponse.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    }
                }

                is MainViewModel.ApiResponse.Success<*> -> {
                    val list = (searchResponse as MainViewModel.ApiResponse.Success).data?.users
                        ?: emptyList()

                    LazyColumn {
                        items(4) {
                            Spacer(Modifier.height(10.dp))
                            Row(
                                modifier
                                    .padding(horizontal = 10.dp)
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                                    .clickable {},
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier.wrapContentSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = list[it].profileImageUrl.ifEmpty {
                                            R.drawable.user
                                        },
                                        contentDescription = "menu",
                                        contentScale = ContentScale.Crop,
                                        modifier = modifier
                                            .size(45.dp)
                                            .aspectRatio(1f, matchHeightConstraintsFirst = true)
                                            .padding(3.dp)
                                            .clip(CircleShape)
                                    )

                                    Spacer(modifier.width(15.dp))

                                    Column {
                                        Text(
//                                    text = "Abhinav Mahalwal",
                                            text = list[it].fullName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Black
                                        )
                                        Spacer(modifier.height(3.dp))

                                        Text(
//                                    text = "_d_evil_02",
                                            text = list[it].username,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            when (exploreState) {
                is MainViewModel.ApiResponse.Failure -> {
                    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    viewModel.fetchFeed()
                                }
                        )
                    }
                }

                MainViewModel.ApiResponse.Idle -> {}

                MainViewModel.ApiResponse.Loading -> {
                    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier.size(40.dp))
                    }
                }

                is MainViewModel.ApiResponse.Success<*> -> {
                    val list =
                        (exploreState as MainViewModel.ApiResponse.Success).data ?: emptyList()

                    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                        items(list) { post ->
                            AsyncImage(
                                modifier = modifier
                                    .padding((0.75).dp)
                                    .aspectRatio(1f)
                                    .clickable {
                                        navController.navigate(Screen.ViewPost(post.id, "Explore"))
                                    },
                                model = post.postUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SearchPreview() {
    Search(
        navController = rememberNavController(),
        viewModel = MainViewModel(RetrofitInstanceMain.getApiService(""))
    )
}

@Composable
fun SearchBar(
    query: String,
    modifier: Modifier,
    onClickFocus: () -> Unit,
    onQueryChange: (new: String) -> Unit
) {
    Row(
        modifier
            .background(shape = RoundedCornerShape(24.dp), color = WhiteGray)
            .padding(horizontal = 5.dp)
            .clickable {
                onClickFocus
            }
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start,
            ),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size((17.5).dp),
                        tint = Gray
                    )
                    Spacer(Modifier.width(5.dp))
                    Box(Modifier.fillMaxWidth()) {
                        if (query.isEmpty()) {
                            Text(
                                "Search with Meta Ai",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()

                        if (!query.isEmpty()) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                                tint = Gray,
                                modifier = Modifier
                                    .size((17.5).dp)
                                    .align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            },
        )
    }
}