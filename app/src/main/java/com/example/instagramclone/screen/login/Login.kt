package com.example.instagramclone.screen.login

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.R
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.Gray
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.viewmodel.LoginViewModel

@Composable
fun Login(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: LoginViewModel,
    launchActivity: (token: String) -> Unit
) {
    var textNameState by remember {
        mutableStateOf("")
    }

    var textPasswordState by remember {
        mutableStateOf("")
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
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
    val loginState by viewModel.uiState.collectAsState()

    // Handle login state changes with LaunchedEffect to prevent repeated toasts
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginViewModel.LoginState.Error -> {
                Toast.makeText(
                    context,
                    "Incorrect username or password",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is LoginViewModel.LoginState.Success -> {
                val token = (loginState as LoginViewModel.LoginState.Success).token
                val saved = SessionManager.saveToken(context.applicationContext, token = token)
                if (saved) {
                    launchActivity(token)
                } else {
                    Toast.makeText(context, "Error saving token", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
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
                fontWeight = FontWeight.Normal
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            label = {
                Text(
                    "Username, email or mobile number",
                    fontSize = labelFontSizeName.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
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
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            interactionSource = interactionSourceUsername
        )
        Spacer(modifier.height(10.dp))

        // Fixed Password Field
        TextField(
            value = textPasswordState,
            onValueChange = { textPasswordState = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            label = {
                Text(
                    "Password",
                    fontSize = labelFontSizePass.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation('\u25cf')
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(
                        if (isPasswordVisible) {
                            R.drawable.visibility_off
                        } else {
                            R.drawable.visibility_on
                        }
                    ),
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            isPasswordVisible = !isPasswordVisible
                        }
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
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            interactionSource = interactionSourcePassword,
        )

        Spacer(modifier.height(10.dp))
        Button(
            onClick = {
                if (textNameState.isEmpty() || textPasswordState.isEmpty()) {
                    Toast.makeText(context, "Please fill all the details", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (textNameState.contains("@gmail.com")) {
                        viewModel.login(LoginRequest(textNameState, null, textPasswordState))
                    } else {
                        viewModel.login(LoginRequest(null, textNameState, textPasswordState))
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
            if (loginState is LoginViewModel.LoginState.Loading) {
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
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screen.ForgotPassword) },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Forgot Password?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }
        Spacer(modifier.weight(1f, true))
        Column(
            modifier
                .padding(bottom = 25.dp)
                .wrapContentHeight()
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(Screen.Register) },
                modifier = modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
                    .height(45.dp)
                    .border(
                        BorderStroke(1.dp, color = Blue),
                        shape = RoundedCornerShape(28.dp)
                    ),
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
                    color = Gray
                )
            }
        }
    }
}