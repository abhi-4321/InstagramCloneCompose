package com.example.instagramclone.screen.login

import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.viewmodel.MainViewModel

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {
    var textNameState by remember {
        mutableStateOf("")
    }

    var textPasswordState by remember {
        mutableStateOf("")
    }

    // Create an interaction source to track focus state
    val interactionSourcePassword = remember { MutableInteractionSource() }
    // Detect if field is focused
    val isFocusedPassword by interactionSourcePassword.collectIsFocusedAsState()

    // Create an interaction source to track focus state
    val interactionSourceUsername = remember { MutableInteractionSource() }
    // Detect if field is focused
    val isFocusedUsername by interactionSourceUsername.collectIsFocusedAsState()

    val labelFontSizeName by animateFloatAsState(
        targetValue = if (isFocusedUsername || textNameState.isNotEmpty()) 12f else 16f,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    )

    val labelFontSizePass by animateFloatAsState(
        targetValue = if (isFocusedPassword || textPasswordState.isNotEmpty()) 12f else 16f,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    )

    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    val loginState by viewModel.uiState.collectAsState()

    when (loginState) {
        is MainViewModel.LoginState.Error -> {
            Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
        }

        is MainViewModel.LoginState.Success -> {
            sessionManager.saveAuthToken(token = (loginState as MainViewModel.LoginState.Success).token)
            viewModel.initOrUpdateRetrofit(context.applicationContext)
            LaunchedEffect(Unit) {
                navController.navigate(Screen.Home)
            }
        }

        else -> {}
    }

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
        TextField(
            value = textNameState,
            onValueChange = { textNameState = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                letterSpacing = TextUnit(0f, TextUnitType.Sp),
                fontWeight = FontWeight.Normal
            ),
            label = {
                Text(
                    "Username, email or mobile number",
                    fontSize = labelFontSizeName.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp)
                )
            },
            modifier = modifier
                .padding(horizontal = 15.dp)
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
            interactionSource = interactionSourceUsername
        )
        Spacer(modifier.height(10.dp))
        TextField(
            value = textPasswordState,
            onValueChange = { textPasswordState = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                letterSpacing = TextUnit(0f, TextUnitType.Sp),
                fontWeight = FontWeight.Normal
            ),
            label = {
                Text(
                    "Password",
                    fontSize = labelFontSizePass.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    letterSpacing = TextUnit(0f, TextUnitType.Sp)
                )
            },
            modifier = modifier
                .padding(horizontal = 15.dp)
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
            interactionSource = interactionSourcePassword
        )
        Spacer(modifier.height(10.dp))
        Button(
            onClick = {
                if (textNameState.isEmpty() || textPasswordState.isEmpty()) {
                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.login(LoginRequest(textNameState, textPasswordState))
                }
            },
            modifier = modifier
                .padding(horizontal = 15.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            if (loginState is MainViewModel.LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    modifier = modifier.padding(vertical = 4.dp),
                    text = "Log in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
            }
        }
        Spacer(modifier.height(20.dp))
        Row(modifier = modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Screen.ForgotPassword) }, horizontalArrangement = Arrangement.Center) {
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
                onClick = { navController.navigate(Screen.Register) },
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