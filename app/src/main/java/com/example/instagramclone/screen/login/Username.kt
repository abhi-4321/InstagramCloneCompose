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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Green
import com.example.instagramclone.ui.theme.MoreLightGray

@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
fun Username(modifier: Modifier = Modifier, navController: NavHostController) {
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
                .offset(x = (-2).dp)
                .clickable { navController.navigateUp() }
            ,
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "Create a username",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = TextUnit(0f, TextUnitType.Sp)
        )
        Spacer(modifier.height(8.dp))
        Text(
            "Add a username or use our suggestion. You can change this at any time.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0f, TextUnitType.Sp)
        )
        Spacer(modifier.height(20.dp))
        TextField(
            value = textName,
            onValueChange = { textName = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                letterSpacing = TextUnit(0f, TextUnitType.Sp),
                fontWeight = FontWeight.Normal
            ),
            label = {
                Text(
                    "Username",
                    fontSize = if (textName.isEmpty()) 16.sp else 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp)
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    BorderStroke(1.dp, MoreLightGray),
                    RoundedCornerShape(15.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // Make background transparent
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.Black, // Set cursor color
                focusedIndicatorColor = Color.Transparent,  // Remove bottom line when focused
                unfocusedIndicatorColor = Color.Transparent,  // Remove bottom line when not focused
                disabledIndicatorColor = Color.Transparent,  // Remove bottom line when disabled
            ),
            trailingIcon = {
                if (textName.isNotEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_check_circle_outline_24),
                        contentDescription = "check",
                        modifier
                            .size(20.dp)
                            .scale(1.2f),
                        tint = Green
                    )
                }
            }
        )
        Spacer(modifier.height(15.dp))
        Button(
            onClick = { navController.navigate(Screen.TermsAndPolicies) },
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
        Row(modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .clickable {
                navController.popBackStack(Screen.Login, false)
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