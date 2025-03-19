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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.MoreLightGray

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun Register(modifier: Modifier = Modifier, navController: NavHostController) {
    var textMobState by remember {
        mutableStateOf("")
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
                .offset(x = (-2).dp)
                .clickable { navController.navigateUp() }
            ,
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "What's your email?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier.height(8.dp))
        Text(
            "Enter the email id where you can be contacted. No one will see this on your profile.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(20.dp))
        TextField(
            value = textMobState,
            onValueChange = { textMobState = it },
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            label = {
                Text(
                    "Username",
                    fontSize = labelFontSizeName.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            },
            modifier = modifier.fillMaxWidth().height(52.dp).border(BorderStroke(1.dp, MoreLightGray),
                RoundedCornerShape(15.dp)
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,  // Make background transparent
                focusedIndicatorColor = Color.Transparent,  // Remove bottom line when focused
                unfocusedIndicatorColor = Color.Transparent,  // Remove bottom line when not focused
                disabledIndicatorColor = Color.Transparent,  // Remove bottom line when disabled
                cursorColor = Color.Black // Set cursor color
            ),
            interactionSource = interactionSourceUsername
        )
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                if (textMobState.isEmpty()) {
                    
                }
                navController.navigate(Screen.EnterPassword)
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
        Spacer(modifier.height(10.dp))
        Button(
            onClick = { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() },
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
                text = "Sign up with mobile number",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.weight(1f, true))
        Row(modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .clickable {
                navController.popBackStack(Screen.Login, false)
            }, horizontalArrangement = Arrangement.Center
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