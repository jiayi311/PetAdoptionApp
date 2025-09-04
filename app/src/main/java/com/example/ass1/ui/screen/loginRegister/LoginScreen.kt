package com.example.ass1.ui.screen.loginRegister

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ass1.R
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun LoginScreen(
    loginRegisterViewModel: LoginRegisterViewModel,
    onLoginSuccess: () -> Unit,
    onLoginAdmin: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loginUiState by loginRegisterViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = loginUiState.isLoading
    val dogOnLoginPage = painterResource(R.drawable.dog_1)
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
        loginUiState.isLogin,
        loginUiState.isLoading,
        loginUiState.isLoginSuccessful
    ) {
        // Only proceed if login was made and loading has finished
        if (loginUiState.isLogin && !loginUiState.isLoading) {
            if (loginUiState.isLoginSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Login successful!!",
                        duration = SnackbarDuration.Short
                    )
                    delay(200)
                    if(loginUiState.isAdmin) {
                        onLoginAdmin()
                    }else{onLoginSuccess()}
                    loginRegisterViewModel.resetLoginRegisterForm()

                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Login failed. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                    loginRegisterViewModel.resetLoginRegisterForm()
                    delay(500)
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

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)

        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                FureverFriendsTitle()

                Box() {

                    //Cartoon Dog Image
                    ImageOnLoginRegisterPage(dogOnLoginPage, modifier = Modifier.size(220.dp))

                    //Login Form
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 80.dp),
                        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4e84cc))
                    ) {

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .padding(top = 152.dp, start = 32.dp, end = 32.dp)
                        ) {
                            Subtitle(stringResource(R.string.log_in))

                            Spacer(modifier = Modifier.height(16.dp))

                            Column() {
                                Text(
                                    text = stringResource(R.string.email),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontFamily = poppinsFontFamily
                                    ),
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                TextField(
                                    value = loginUiState.userEmail,
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

                                Text(
                                    text = stringResource(R.string.password),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontFamily = poppinsFontFamily
                                    ),
                                    modifier = Modifier.padding(start = 12.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                TextField(
                                    value = loginUiState.userPw,
                                    onValueChange = { loginRegisterViewModel.updatePassword(it) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFbfdaf4),
                                        unfocusedContainerColor = Color(0xFFdaedff),
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.enter_password),
                                            style = TextStyle(
                                                color = Color.Gray
                                            )
                                        )
                                    },
                                    visualTransformation = if (loginUiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            loginRegisterViewModel.updatePwVisibility(!loginUiState.passwordVisible)
                                        }) {
                                            Icon(
                                                imageVector = if (loginUiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = if (loginUiState.passwordVisible) "Hide password" else "Show password"
                                            )
                                        }
                                    }
                                )

                                //Forgot Password
                                TextButton(
                                    onClick = onForgotPassword,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                ) {
                                    Text(
                                        text = stringResource(R.string.forgot_password),
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            fontStyle = FontStyle.Italic,
                                            fontFamily = poppinsFontFamily,
                                            textDecoration = TextDecoration.Underline
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.End
                                    )
                                }

                                Spacer(modifier = Modifier.height(72.dp))

                                Button(
                                    onClick = {
                                        val email = loginUiState.userEmail
                                        val password = loginUiState.userPw

                                        if (loginRegisterViewModel.validateLoginForm(email, password)) {
                                            loginRegisterViewModel.signIn(email,password)
                                        } else {
                                            scope.launch {
                                                loginUiState.errorMessage?.let {
                                                    snackbarHostState.showSnackbar(
                                                        message = it,
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .width(144.dp)
                                        .height(48.dp)
                                        .align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(32.dp),
                                    colors = ButtonDefaults.buttonColors(Color.White)
                                ) {
                                    Text(
                                        text = stringResource(R.string.login),
                                        style = TextStyle(
                                            fontSize = 20.sp,
                                            color = Color(0xFF0c4485),
                                            fontFamily = poppinsFontFamily,
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    Text(
                                        text = stringResource(R.string.don_t_have_an_account),
                                        style = TextStyle(
                                            fontFamily = poppinsFontFamily,
                                            color = Color.White,
                                            fontSize = 12.sp,
                                        )
                                    )

                                    Spacer(modifier = Modifier.width(4.dp))

                                    Text(
                                        text = stringResource(R.string.create_an_account),
                                        style = TextStyle(
                                            color = Color(0xFFffde59),
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 12.sp
                                        ),
                                        modifier = Modifier
                                            .clickable(onClick = onRegister)
                                    )

                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}
