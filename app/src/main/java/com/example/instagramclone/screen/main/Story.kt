package com.example.instagramclone.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.LightGray

@Preview(showSystemUi = true)
@Composable
fun Story(modifier: Modifier = Modifier) {

    var textMessageState by remember {
        mutableStateOf("")
    }

    Box(
        modifier
            .background(Color.Black)
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .fillMaxHeight()
    ) {
        Image(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f),
            painter = painterResource(R.drawable.p),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )

        Column(
            modifier = modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
        ) {
            LinearProgressIndicator(
                progress = { 0.5F },
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
                        Image(
                            painter = painterResource(R.drawable.p),
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
                                text = "Abhinav Mahalwal",
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
                                text = "_d_evil_02",
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

            Box(modifier.padding(horizontal = 5.dp).fillMaxWidth()) {
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

                Row(modifier.align(Alignment.TopEnd).padding(end = 5.dp, top = 7.dp)) {
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