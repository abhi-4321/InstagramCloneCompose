package com.example.instagramclone.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray

@Composable
fun HighlightTitle(url: String) {
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(Color.White)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
                Text(
                    text = "Title",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Done",
                fontSize = 18.sp,
                color = Blue,
                fontWeight = FontWeight.Bold
            )
        }


        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            RoundImage(url,Modifier.size(120.dp))
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Edit cover",
                fontSize = 15.sp,
                color = Gray,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(15.dp))
            Text(
                text = "Highlight",
                fontSize = 21.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )


        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HighlightTitlePreview() {
    HighlightTitle("")
}

