package com.example.instagramclone.screen.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.Blue

@Composable
fun AddHighlight() {

    var isChecked by remember {
        mutableStateOf(false)
    }

    Column(
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
                    text = "New highlight",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Next",
                fontSize = 18.sp,
                color = Blue,
                fontWeight = FontWeight.Bold
            )
        }

        LazyVerticalGrid(columns = GridCells.Fixed(3)) {
            items(15) {
                Box {
                    Image(
                        modifier = Modifier
                            .padding((0.75).dp)
                            .size(180.dp),
                        painter = painterResource(R.drawable.p),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )

                    androidx.compose.material3.Checkbox(
                        checked = isChecked,
                        modifier = Modifier
                            .padding(5.dp)
                            .size(30.dp)
                            .align(Alignment.BottomEnd),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.Gray,
                            uncheckedColor = Color.Gray
                        ),
                        onCheckedChange = {
                            isChecked = it
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AddHighlightPreview() {
    AddHighlight()
}


