package com.example.instagramclone.screen.login

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.instagramclone.R
import com.example.instagramclone.model.OtpRequest
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.viewmodel.LoginViewModel


//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun Confirmation(modifier: Modifier = Modifier, navController: NavHostController, viewModel: LoginViewModel) {
    var textMobState by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val otpRequestState by viewModel.otpState.collectAsState()

    when (otpRequestState) {
        LoginViewModel.OtpRequestState.Error -> {
            Toast.makeText(context, "Unknown error occurred. Please retry", Toast.LENGTH_SHORT).show()
        }
        LoginViewModel.OtpRequestState.Sent -> {
            Toast.makeText(context, "Otp sent to email successfully", Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    val otpVerifyState by viewModel.otpVerifyState.collectAsState()

    when (otpVerifyState) {
        LoginViewModel.OtpVerifyState.Correct -> {
            LaunchedEffect(true) {
                navController.navigate(Screen.EnterPassword)
            }
        }
        LoginViewModel.OtpVerifyState.Error -> {
            Toast.makeText(context, "Unknown error occurred. Please retry", Toast.LENGTH_SHORT).show()
        }
        LoginViewModel.OtpVerifyState.Incorrect -> {
            Toast.makeText(context, "Otp is invalid or expired", Toast.LENGTH_SHORT).show()
        }
        else -> {}
    }

    // Create an interaction source to track focus state
    val interactionSourceUsername = remember { MutableInteractionSource() }
    // Detect if field is focused
    val isFocusedUsername by interactionSourceUsername.collectIsFocusedAsState()

    val labelFontSizeName by animateFloatAsState(
        targetValue = if (isFocusedUsername || textMobState.isNotEmpty()) 12f else 16f,
        animationSpec = tween(
            durationMillis = 150,
            easing = LinearEasing
        )
    )

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
                .clickable { navController.navigateUp() },
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "Enter the confirmation code",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = TextUnit(0f, TextUnitType.Sp)
        )
        Spacer(modifier.height(8.dp))
        Text(
            "To confirm your account, enter the 6-digit code we sent to your email",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = TextUnit(0f, TextUnitType.Sp)
        )
        Spacer(modifier.height(20.dp))
        TextField(
            value = textMobState,
            onValueChange = { textMobState = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            ),
            label = {
                Text(
                    "Confirmation code",
                    fontSize = labelFontSizeName.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    BorderStroke(1.dp, MoreLightGray),
                    RoundedCornerShape(15.dp)
                ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // Prevents multiline actions
            ),
            singleLine = true, // Ensures it's a single-line field
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
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                if (textMobState.isEmpty()) {
                    Toast.makeText(context, "Enter a valid otp", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.verifyOtp(otp = textMobState)
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            if (otpVerifyState is LoginViewModel.OtpVerifyState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    modifier = modifier.padding(vertical = 4.dp),
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
            }
        }
        Spacer(modifier.height(10.dp))
        Button(
            onClick = {
                viewModel.sendOtp()
            },
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
                text = "I didn't get the code",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
    }
}