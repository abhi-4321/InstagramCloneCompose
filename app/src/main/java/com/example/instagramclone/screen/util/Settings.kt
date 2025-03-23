package com.example.instagramclone.screen.util

import android.widget.Toast
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.network.util.SessionManager
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.viewmodel.MainViewModel

@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", )
fun Settings(modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel, launchActivity: () -> Unit) {

    val context = LocalContext.current

    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier
        .fillMaxSize()
        .padding(20.dp)) {
        Button(
            onClick = {
//                viewModel.saveToken(null)
                val saved = SessionManager.saveToken(context.applicationContext,null)
                if (saved) {
                    launchActivity()
                } else {
                    Toast.makeText(context, "Error logging out", Toast.LENGTH_SHORT).show()
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
        }
    }

}