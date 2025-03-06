package com.example.instagramclone.screen.login

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.DisableContentCapture
import androidx.compose.ui.platform.LocalContext
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
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.screen.Dashboard
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.InstagramCloneTheme
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun Login(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel) {
    var textNameState by remember {
        mutableStateOf("")
    }

    var textPasswordState by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    InstagramCloneTheme {
        Column(
            modifier = modifier
                .statusBarsPadding()
                .fillMaxSize()
                .background(Color.White)
        ) {
            Spacer(modifier.height(40.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "English (US)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp),
                    style = TextStyle(color = Gray)
                )
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_down_24),
                    contentDescription = "down",
                    tint = Gray
                )
            }
            Spacer(modifier.height(85.dp))
            Row(
                modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.instagram),
                    contentDescription = "Instagram",
                    modifier = modifier.size(70.dp),
                )
            }
            Spacer(modifier.height(110.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .padding(horizontal = 15.dp)
                    .border(
                        border = BorderStroke(1.dp, MoreLightGray),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            ) {
                BasicTextField(value = textNameState,
                    onValueChange = { textNameState = it },
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    textStyle = TextStyle(
                        Color.Black, 16.sp,
                        FontWeight.Normal,
                        letterSpacing = TextUnit(0f, TextUnitType.Sp)
                    ),
                    decorationBox = {
                        Box {
                            if (textNameState.isEmpty())
                                Text(
                                    "Username, email or mobile number",
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
            Spacer(modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = modifier
                    .padding(horizontal = 15.dp)
                    .border(
                        border = BorderStroke(1.dp, MoreLightGray),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
            ) {
                BasicTextField(
                    value = textPasswordState,
                    onValueChange = { textPasswordState = it },
                    modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    textStyle = TextStyle(
                        Color.Black, 16.sp,
                        FontWeight.Normal,
                        letterSpacing = TextUnit(0f, TextUnitType.Sp)
                    ),
                    decorationBox = {
                        Box {
                            if (textPasswordState.isEmpty())
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
            Spacer(modifier.height(10.dp))
            Button(
                onClick = {
                    if (textNameState.isEmpty() || textPasswordState.isEmpty()) {
                        Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = viewModel.login(LoginRequest(textNameState, textPasswordState), context)
                            withContext(Dispatchers.Main) {
                                if (response) {
                                    navController.navigate(Dashboard)
                                } else {
                                    Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                modifier = modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    modifier = modifier.padding(vertical = 4.dp),
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
            }
            Spacer(modifier.height(20.dp))
            Row(modifier = modifier
                .fillMaxWidth()
                .clickable { navController.navigate(ForgotPassword) }, horizontalArrangement = Arrangement.Center) {
                Text(
                    "Forgot Password?",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp)
                )
            }
            Spacer(modifier.weight(1f, true))
            Column(modifier
                .padding(bottom = 25.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                , horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate(Register) },
                    modifier = modifier
                        .padding(horizontal = 15.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .border(
                            BorderStroke(1.dp, color = Blue),
                            shape = RoundedCornerShape(28.dp)
                        )
                    ,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(
                        text = "Create new account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Blue,
                        letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                    )
                }
                Spacer(modifier.height(10.dp))
                Row(modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.meta_black_icon),
                        contentDescription = "Meta",
                        tint = Gray
                    )
                    Spacer(modifier.width(5.dp))
                    Text(
                        "Meta",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray,
                        letterSpacing = TextUnit(0f, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

@Serializable
object Login