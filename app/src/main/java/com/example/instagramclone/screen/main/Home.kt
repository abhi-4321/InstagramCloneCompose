package com.example.instagramclone.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.viewmodel.MainViewModel

@Composable
fun Home(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Row(
            modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Button(
                onClick = { },
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    modifier = modifier.padding(vertical = 4.dp),
                    text = "Dashboard",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
            }
            /*Button(
                onClick = {
                    sessionManager.clear()
                    viewModel.initOrUpdateRetrofit(context.applicationContext)
                    navController.navigate(Login) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
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
                    text = "Log out",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = TextUnit(0.5f, TextUnitType.Sp)
                )
            }*/
        }
    }
}