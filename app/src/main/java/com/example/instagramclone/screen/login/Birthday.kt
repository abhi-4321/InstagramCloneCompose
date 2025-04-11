package com.example.instagramclone.screen.login

import android.app.DatePickerDialog
import android.widget.DatePicker
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.instagramclone.R
import com.example.instagramclone.navigation.Screen
import com.example.instagramclone.ui.theme.Blue
import com.example.instagramclone.ui.theme.MoreLightGray
import com.example.instagramclone.ui.theme.Pink40
import com.example.instagramclone.ui.theme.Purple80
import com.example.instagramclone.ui.theme.PurpleGrey80
import com.example.instagramclone.viewmodel.LoginViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
fun Birthday(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: LoginViewModel
) {
    val context = LocalContext.current

    var textBirthday by remember {
        mutableStateOf("")
    }

    val calendar = remember {
        Calendar.getInstance()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = calendar.timeInMillis,
        yearRange = (calendar.get(Calendar.YEAR) - 100)..calendar.get(Calendar.YEAR)
    )

    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) { // Fix: Avoid null crash
                            val selectedDate = Calendar.getInstance().apply {
                                timeInMillis = selectedMillis
                            }

                            val today = Calendar.getInstance()

                            if (selectedDate.after(today)) { // Fix: Correct logic
                                Toast.makeText(
                                    context,
                                    "Selected date should be in the past. Please select again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                textBirthday =
                                    SimpleDateFormat("yyyy/MM/dd").format(selectedDate.time)
                                showDatePicker = false
                            }
                        }

                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) { Text("Cancel") }
            },
            colors = DatePickerDefaults.colors(
                containerColor = PurpleGrey80,
            )
        )
        {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    todayContentColor = Color.Black,
                    todayDateBorderColor = Purple80,
                    selectedDayContentColor = Color.Black,
                    dayContentColor = Color.Black,
                    selectedDayContainerColor = Purple80,
                )
            )
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

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
            "What's your birthday?",
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier.height(8.dp))
        Text(
            "Use your own birthday, even if this account is for a business, a pet or something else. No one will see this unless you choose to share it. Why do I need to provide my birthday?",
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier.height(20.dp))
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp)
                .border(
                    BorderStroke(1.dp, MoreLightGray),
                    RoundedCornerShape(15.dp)
                )
                .clickable(interactionSource = interactionSource, indication = null) {
                    showDatePicker = true
                }
                .padding(horizontal = 16.dp), // Ensuring padding inside Box
            contentAlignment = androidx.compose.ui.Alignment.CenterStart // Align text like TextField
        ) {
            Text(
                text = if (textBirthday.isEmpty()) "Birthday" else textBirthday,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = if (textBirthday.isEmpty()) Color.Gray else Color.Black
            )

            Icon(
                painter = painterResource(R.drawable.baseline_calendar_month_24),
                contentDescription = null,
                modifier = Modifier
                    .align(androidx.compose.ui.Alignment.CenterEnd)
                    .padding(end = 8.dp) // Keep same spacing for the trailing icon
            )
        }
        Spacer(modifier.height(15.dp))
        Button(
            onClick = {
                if (textBirthday.isEmpty()) {
                    Toast.makeText(context, "Please enter a valid date", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.registrationDetails.birthday = textBirthday
                navController.navigate(Screen.Name)
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
        Spacer(modifier.weight(1f, true))
        Row(
            modifier
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