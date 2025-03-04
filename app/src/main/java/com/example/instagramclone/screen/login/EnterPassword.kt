package com.example.instagramclone.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.screen.Dashboard
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.MoreLightGray
import kotlinx.serialization.Serializable

@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
fun EnterPassword(modifier: Modifier = Modifier, navController: NavController) {
    var textName by remember {
        mutableStateOf("")
    }

    Column(
        modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(15.dp),
    ) {

        Icon(
            painter = painterResource(id = R.drawable.baseline_keyboard_backspace_24),
            contentDescription = null,
            modifier = modifier
                .size(18.dp)
                .offset(x = (-2).dp),
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "Create a password",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier.height(8.dp))
        Text(
            "Create a password with at least 6 letters or numbers. It should be something others can't guess.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier
                .border(
                    border = BorderStroke(1.dp, MoreLightGray),
                    shape = RoundedCornerShape(15.dp)
                )
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
        ) {
            BasicTextField(value = textName,
                onValueChange = { textName = it },
                modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textStyle = TextStyle(
                    Color.Black, 16.sp,
                    FontWeight.Bold,
                ),
                decorationBox = {
                    Box {
                        if (textName.isEmpty())
                            Text(
                                "Password",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Gray,
                                letterSpacing = TextUnit(0f, TextUnitType.Sp)
                            )
                        it()
                    }
                })
        }
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                when (navController.previousBackStackEntry?.destination?.route) {
                    Login.toString() -> {
                        navController.navigate(Dashboard)
                    }
                    Confirmation.toString() -> {
                        navController.navigate(SaveInfo)
                    }
                    else -> {
                    }
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                modifier = modifier.padding(vertical = 4.dp),
                text = "Next",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.weight(1f, true))
        Row(modifier.fillMaxWidth().padding(vertical = 20.dp).clickable {
            navController.popBackStack(Login, false)
        }, horizontalArrangement = Arrangement.Center) {
            Text(
                text = "I already have an account",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Blue,
                letterSpacing = TextUnit(0f, TextUnitType.Sp)
            )
        }
    }
}

@Serializable
object EnterPassword