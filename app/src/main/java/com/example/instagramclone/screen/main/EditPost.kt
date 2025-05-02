package com.example.instagramclone.screen.main

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.viewmodel.MainViewModel

@Composable
fun EditPost(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    uri: String
) {
    var textCaptionState by remember { mutableStateOf("") }

    Column(
        modifier
            .statusBarsPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
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

            Spacer(modifier.height(20.dp))

            Image(
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop,
                painter = rememberImagePainter(data = uri.toUri())
            )

            Spacer(modifier.height(10.dp))

            BasicTextField(
                value = textCaptionState,
                onValueChange = { textCaptionState = it },
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Start,
                ),
                maxLines = 3,
                decorationBox = { innerTextField ->
                    if (textCaptionState.isEmpty()) {
                        Text(
                            "Add a caption...",
                            color = Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            )
        }

        Row(
            modifier = modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable {
                    viewModel.createPost(uri)
                }
                .background(color = Blue,shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
            ) {
            Text("Share", fontSize = 15.sp, modifier = modifier.padding(vertical = 10.dp, horizontal = 5.dp), color = Color.White, fontWeight = FontWeight.Medium)
        }
    }
}
