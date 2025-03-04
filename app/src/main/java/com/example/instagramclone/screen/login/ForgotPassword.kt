package com.example.instagramclone.screen.login

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.MoreLightGray
import kotlinx.serialization.Serializable

@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
fun ForgotPassword(modifier: Modifier = Modifier, navController: NavController) {
    var textName by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

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
            "Find your account",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier.height(8.dp))
        Text(
            "Enter your email or username.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
        )
        Spacer(modifier.height(3.dp))
        Text(
            "Can't reset your password?",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            style = TextStyle(color = Blue)
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
                                "Email or username",
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
            onClick = { navController.navigate(Confirmation) },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                modifier = modifier.padding(vertical = 3.dp),
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.height(15.dp))
        Row(modifier.fillMaxWidth().clickable { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }, horizontalArrangement = Arrangement.Center) {
            Text(
                "Search by mobile number instead",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(color = Color.Gray)
            )
        }
        Spacer(modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = MoreLightGray
            )
            Text(
                text = "OR",
                modifier = Modifier.padding(horizontal = 10.dp) // Add spacing around OR
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp),
                color = MoreLightGray
            )
        }
        Spacer(modifier.height(10.dp))
        Row(
            modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, color = MoreLightGray), RoundedCornerShape(28.dp)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = "facebook",
                tint = Color.Black,
                modifier = modifier.size(17.dp)
            )
            Text(
                modifier = Modifier.padding(start = 8.dp).wrapContentSize().padding(vertical = 10.dp),
                text = "Log in with Facebook",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
        Spacer(modifier.weight(1f, true))
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp).clickable {
                    navController.popBackStack(Login, false)
                },
            horizontalArrangement = Arrangement.Center
        ) {
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
object ForgotPassword

