package com.example.instagramclone.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.WhiteVar
import kotlinx.serialization.Serializable
import java.io.Serial

@Preview(showSystemUi = true)
@Composable
fun Home() {
    Column(
        Modifier
            .fillMaxSize()
            .background(WhiteVar)
            .statusBarsPadding()
    ) {
        Toolbar()
        MainContent()
        Spacer(modifier = Modifier.weight(1f,true))
        Button()
    }
}

@Composable
fun Toolbar(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(15.dp), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                contentDescription = null,
                modifier = modifier
                    .scale(1f),
                tint = Color.Black
            )
            Text(
                text = "Back",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = "Cancel",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MainContent() {
    var textTitleState by remember {
        mutableStateOf("")
    }

    Spacer(modifier = Modifier.height(80.dp))

    Row(modifier = Modifier.fillMaxWidth().wrapContentHeight(), horizontalArrangement = Arrangement.Center) {
        Text(
            text = "Choose a Name",
            fontSize = 28.sp,
            fontWeight = FontWeight.W900,
        )
    }

    Spacer(modifier = Modifier.height(40.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)) // TextField background color
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        BasicTextField(value = textTitleState,
            onValueChange = { textTitleState = it },
            Modifier.wrapContentSize(),
            textStyle = TextStyle(
                Color.Black, 16.sp,
                FontWeight.Bold
            ),
            decorationBox = {
                Box {
                    if (textTitleState.isEmpty())
                        Text("Enter your name", fontSize = 16.sp, color = Color.Gray)
                    it()
                }
            })
    }

    Spacer(modifier = Modifier.height(15.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)) // TextField background color
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 6.dp),
    ) {
        Image(
            painter = painterResource(id = R.drawable.p),
            contentDescription = "menu",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .padding(3.dp)
                .clip(CircleShape)
        )

        Text(
            text = "Add a Photo (optional)",
            fontSize = 16.sp,
            fontWeight = FontWeight.W900,
            style = TextStyle(color = Blue),
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun Button() {
    Button(
        onClick = {  },
        modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 65.dp)
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            text = "Continue",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
    }
}

@Serializable
object Home