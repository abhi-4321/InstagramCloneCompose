package com.example.instagramclone.screen.login

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.viewmodel.LoginViewModel

//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Composable
fun TermsAndPolicies(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiStateReg.collectAsState()

    when(uiState) {
        is LoginViewModel.LoginState.Error -> {
            Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_SHORT).show()
        }
        is LoginViewModel.LoginState.Success -> {
            LaunchedEffect(true) {
                val token = (uiState as LoginViewModel.LoginState.Success).token
                val saved = SessionManager.saveToken(context.applicationContext, token = token)
                if (saved) {
                    navController.navigate(Screen.ProfilePicture(token))
                } else {
                    Toast.makeText(context,"Error saving token", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else -> {}
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
                .clickable { navController.navigateUp() }
                .offset(x = (-2).dp),
            tint = Color.Black,
        )
        Spacer(modifier.height(15.dp))
        Text(
            "Agree to Instagram's terms and policies",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier.height(8.dp))
        Text(
            "People who use our services may have uploaded your contact information to Instagram. Learn More",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(15.dp))
        Text(
            "By tapping I agree, you have to create an account and to Instagram's Terms, Privacy Policy and Cookies Policy.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(15.dp))
        Text(
            "The Privacy Policy describes the ways we can use the infromation we collect when you create an account. For information we collect when you create an account. For example, we use this information to provide, personalize and improve our products, including ads.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                viewModel.register()
            },
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                modifier = modifier.padding(vertical = 4.dp),
                text = "I agree",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
            )
        }
        Spacer(modifier.weight(1f, true))
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .clickable {
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

@Preview(showSystemUi = true)
@Composable
fun TermsAndPoliciesPreview() {
    TermsAndPolicies(
        navController = rememberNavController(), viewModel = LoginViewModel(
            RetrofitInstanceLogin.instance
        )
    )
}