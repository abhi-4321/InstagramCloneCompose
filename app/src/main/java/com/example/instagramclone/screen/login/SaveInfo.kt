package com.example.instagramclone.screen.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.login.RetrofitInstanceLogin
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.viewmodel.LoginViewModel

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun SaveInfo(modifier: Modifier = Modifier, navController: NavHostController) {
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
                .clickable(
                    indication = null,
                    interactionSource = null
                ) { navController.navigateUp() }
                .offset(x = (-2).dp),
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "Save your login info?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier.height(8.dp))
        Text(
            "We'll save the login info for your new account, so you won't need to enter it next time you log in.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                saveInfo()
                navController.navigate(Screen.Birthday)
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                modifier = modifier.padding(vertical = 4.dp),
                text = "Save",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.height(10.dp))
        Button(
            onClick = { navController.navigate(Screen.Birthday) },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                    BorderStroke(1.dp, color = MoreLightGray),
                    shape = RoundedCornerShape(28.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Text(
                text = "Not now",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.weight(1f, true))
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp).clickable(
                    indication = null,
                    interactionSource = null
                ) {
                    navController.popBackStack(Screen.Login, false)
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "I already have an account",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Blue
            )
        }
    }
}

fun saveInfo() {
}

@Preview(showSystemUi = true)
@Composable
fun SaveInfoPreview() {
    SaveInfo(
        navController = rememberNavController()
    )
}