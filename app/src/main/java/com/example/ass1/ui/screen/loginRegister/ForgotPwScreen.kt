package com.example.ass1.ui.screen.loginRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ForgotPwScreen(
    onNavigateToLogin: () -> Unit,
    loginRegisterViewModel: LoginRegisterViewModel,
    onBackToLogin: () -> Unit
) {

    val forgotPwUiState by loginRegisterViewModel.uiState.collectAsStateWithLifecycle()
    val dogOnRegisterPage = painterResource(R.drawable.dog_2)
    val isLoading =  forgotPwUiState.isLoading
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        forgotPwUiState.isResetPw,
        forgotPwUiState.isLoading,
        forgotPwUiState.isResetPwSuccessful
    ) {
        // Only proceed if reset pw was made and loading has finished
        if (forgotPwUiState.isResetPw && !forgotPwUiState.isLoading) {
            if (forgotPwUiState.isResetPwSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Successfully send the reset password email to your mailbox. Please check it and try to login again.",
                        duration = SnackbarDuration.Short
                    )
                    loginRegisterViewModel.resetLoginRegisterForm()
                    delay(500)
                    onNavigateToLogin()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to send the reset password email to your mailbox. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                    loginRegisterViewModel.resetIsResetPw()
                }
            }
        }
    }

    SetSystemNavColor(0xFF4e84cc, 0xffffffff)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->

        LoginSignUpBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = { onBackToLogin() },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 24.dp, start = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExpandCircleDown,
                            contentDescription = "Back",
                            tint = Color(0xFF4e84cc),
                            modifier = Modifier
                                .size(36.dp)
                                .rotate(90f)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 8.dp)
                    ) {
                        FureverFriendsTitle()
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    //Cartoon Dog Image
                    ImageOnLoginRegisterPage(
                        dogOnRegisterPage, modifier = Modifier
                            .size(160.dp)
                            .zIndex(1f)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        //Reset Pw form
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 132.dp),
                            shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF4e84cc))
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 24.dp, start = 32.dp, end = 32.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Column() {
                                        Text(
                                            text = "Forgot",
                                            style = TextStyle(
                                                color = Color.White,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = poppinsFontFamily
                                            )
                                        )
                                        Text(
                                            text = "Password",
                                            style = TextStyle(
                                                color = Color.White,
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = poppinsFontFamily
                                            )
                                        )
                                    }
                                    Image(
                                        painter = painterResource(R.drawable.white_paw),
                                        contentDescription = "White Paw",
                                        modifier = Modifier
                                            .size(48.dp)
                                            .fillMaxHeight(),
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                //Email field
                                Text(
                                    text = stringResource(R.string.email),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 20.sp,
                                    ),
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                TextField(
                                    value = forgotPwUiState.userEmail,
                                    onValueChange = { loginRegisterViewModel.updateEmail(it) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFbfdaf4),
                                        unfocusedContainerColor = Color(0xFFdaedff),
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.enter_email_address),
                                            style = TextStyle(
                                                color = Color.Gray
                                            )
                                        )
                                    }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, bottom = 8.dp)
                                ) {
                                    Text(
                                        text = "*Please ensure your password fulfilled the requirements below later.",
                                        style = TextStyle(
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontFamily = poppinsFontFamily,
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    PasswordRequirement(stringResource(R.string.password_requirement_1),color = Color(0xff223545))
                                    PasswordRequirement(stringResource(R.string.password_requirement_2),color = Color(0xff223545))
                                    PasswordRequirement(stringResource(R.string.password_requirement_3),color = Color(0xff223545))
                                    PasswordRequirement(stringResource(R.string.password_requirement_4),color = Color(0xff223545))
                                    PasswordRequirement(stringResource(R.string.password_requirement_5),color = Color(0xff223545))
                                }


                                Spacer(modifier = Modifier.height(64.dp))

                                //Reset button
                                Button(
                                    onClick = {
                                        if(loginRegisterViewModel.validateResetPwForm(forgotPwUiState.userEmail)) {
                                            loginRegisterViewModel.resetPassword(forgotPwUiState.userEmail)
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = forgotPwUiState.errorMessage!!,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(200.dp)
                                        .height(48.dp)
                                        .align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = ButtonDefaults.buttonColors(Color.White)
                                ) {
                                    Text(
                                        text = stringResource(R.string.reset_password),
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            color = Color(0xFF0c4485),
                                            fontFamily = poppinsFontFamily,
                                        ),
                                        textAlign = TextAlign.Center
                                    )
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                            }
                        }
                    }
                }
            }
        }
    }

}