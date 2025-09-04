package com.example.ass1.ui.screen.loginRegister

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.ass1.R
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    loginRegisterViewModel: LoginRegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onBackToLogin: () -> Unit
) {
    val registerUiState by loginRegisterViewModel.uiState.collectAsStateWithLifecycle()
    val dogOnRegisterPage = painterResource(R.drawable.dog_2)
    val isLoading =  registerUiState.isLoading

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
        registerUiState.isRegister,
        registerUiState.isLoading,
        registerUiState.isRegistrationSuccessful
    ) {
        // Only proceed if register was made and loading has finished
        if (registerUiState.isRegister && !registerUiState.isLoading) {
            if (registerUiState.isRegistrationSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Registration successful",
                        duration = SnackbarDuration.Short
                    )

                    delay(500)
                    onNavigateToLogin()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Registration failed. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                    loginRegisterViewModel.resetIsRegister()
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

                        //Register form
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
                                Subtitle(stringResource(R.string.sign_up))

                                Spacer(modifier = Modifier.height(16.dp))

                                Column() {

                                    //Full name field
                                    Text(
                                        text = stringResource(R.string.full_name),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 20.sp,
                                        ),
                                        modifier = Modifier.padding(start = 12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    TextField(
                                        value = registerUiState.userName,
                                        onValueChange = { loginRegisterViewModel.updateName(it) },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(32.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFFbfdaf4),
                                            unfocusedContainerColor = Color(0xFFdaedff),
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        placeholder = {
                                            Text(
                                                text = stringResource(R.string.enter_full_name),
                                                style = TextStyle(
                                                    color = Color.Gray
                                                )
                                            )
                                        }
                                    )

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
                                        value = registerUiState.userEmail,
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

                                    Spacer(modifier = Modifier.height(16.dp))

                                    //Phone Number field
                                    Text(
                                        text = stringResource(R.string.phone_number),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 20.sp,),
                                        modifier = Modifier.padding(start = 12.dp)
                                    )

                                    Row(
                                        modifier = Modifier.padding(start = 20.dp)
                                    ) {
                                        Text(
                                            text = " • Format: ",
                                            style = TextStyle(
                                                color = Color(0xff223545),
                                                fontSize = 16.sp,
                                                fontFamily = comicSansFontFamily,
                                            )
                                        )
                                        Column( ) {
                                            Text(
                                                text = stringResource(R.string.phone_number_format1),
                                                style = TextStyle(
                                                    color = Color(0xff223545),
                                                    fontSize = 16.sp,
                                                    fontFamily = comicSansFontFamily
                                                )
                                            )
                                            Text(
                                                text = stringResource(R.string.phone_number_format2),
                                                style = TextStyle(
                                                    color = Color(0xff223545),
                                                    fontFamily = comicSansFontFamily,
                                                    fontSize = 16.sp
                                                )
                                            )
                                        }
                                    }


                                    Spacer(modifier = Modifier.height(8.dp))

                                    TextField(
                                        value = registerUiState.userPhoneUi,
                                        onValueChange = { textFieldValue ->
                                            loginRegisterViewModel.updatePhoneNumber(textFieldValue.text)
                                                        loginRegisterViewModel.phoneValidation(textFieldValue.text)},
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(32.dp),
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFFbfdaf4),
                                            unfocusedContainerColor = Color(0xFFdaedff),
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedIndicatorColor = Color.Transparent
                                        ),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                        placeholder = {
                                            Text(
                                                text = stringResource(R.string.enter_phone_number),
                                                style = TextStyle(
                                                    color = Color.Gray
                                                )
                                            )
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    //Reminder message if the phone number not fulfill all requirements
                                    if(registerUiState.userPhone.isNotBlank() &&
                                        !loginRegisterViewModel.phoneValidation(registerUiState.userPhone)) {
                                        Text(
                                            text = "*"+registerUiState.phoneErrorMessage,
                                            style = TextStyle(
                                                color = Color(0xfff2ff00),
                                                fontFamily = poppinsFontFamily,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    //Password field
                                    /* if not fulfill with any one of the requirements, display an error icon at the end of field,
                                       below the field have to display what requirement haven't be fulfilled */
                                    Text(
                                        text = stringResource(R.string.password),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 20.sp,
                                        ),
                                        modifier = Modifier.padding(start = 12.dp)
                                    )

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 20.dp, bottom = 8.dp)
                                    ) {
                                        PasswordRequirement(stringResource(R.string.password_requirement_1),color = Color(0xff223545))
                                        PasswordRequirement(stringResource(R.string.password_requirement_2),color = Color(0xff223545))
                                        PasswordRequirement(stringResource(R.string.password_requirement_3),color = Color(0xff223545))
                                        PasswordRequirement(stringResource(R.string.password_requirement_4),color = Color(0xff223545))
                                        PasswordRequirement(stringResource(R.string.password_requirement_5),color = Color(0xff223545))
                                    }

                                    TextField(
                                        value = registerUiState.userPw,
                                        onValueChange = { loginRegisterViewModel.updatePassword(it)
                                                        loginRegisterViewModel.passwordValidation(it)},
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
                                                text = stringResource(R.string.enter_password_fulfilled_requirements_above),
                                                style = TextStyle(
                                                    color = Color.Gray
                                                )
                                            )
                                        },
                                        visualTransformation = if (registerUiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                loginRegisterViewModel.updatePwVisibility(!registerUiState.passwordVisible)
                                            }) {
                                                Icon(
                                                    imageVector = if (registerUiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                    contentDescription = if (registerUiState.passwordVisible) "Hide password" else "Show password"
                                                )
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    //Reminder message if the password not fulfill all requirements
                                    if(registerUiState.userPw.isNotBlank() &&
                                        !loginRegisterViewModel.passwordValidation(registerUiState.userPw)) {
                                        Text(
                                            text = "*"+registerUiState.pwErrorMessage,
                                            style = TextStyle(
                                                color = Color(0xfff2ff00),
                                                fontFamily = poppinsFontFamily,
                                                fontSize = 12.sp
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(
                                        text = stringResource(R.string.confirm_password),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 20.sp,
                                        ),
                                        modifier = Modifier.padding(start = 12.dp)
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    TextField(
                                        value = registerUiState.confPw,
                                        onValueChange = { loginRegisterViewModel.updateConfirmPassword(it) },
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
                                                text = stringResource(R.string.enter_password_again),
                                                style = TextStyle(
                                                    color = Color.Gray
                                                )
                                            )
                                        },
                                        visualTransformation = if (registerUiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                        trailingIcon = {
                                            IconButton(onClick = {
                                                loginRegisterViewModel.updatePwVisibility(!registerUiState.passwordVisible)
                                            }) {
                                                Icon(
                                                    imageVector = if (registerUiState.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                    contentDescription = if (registerUiState.passwordVisible) "Hide password" else "Show password"
                                                )
                                            }
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    //Confirm Password field
                                    /* if not same with password above, display a error icon at the end of field
                                    * below the field, show "The confirm password is not same as the password above" */

                                    if(registerUiState.userPw.isNotBlank() &&
                                        registerUiState.confPw.isNotBlank()) {
                                        if(registerUiState.userPw != registerUiState.confPw) {
                                            Text(
                                                text = "*The confirm password is not same as the password above.",
                                                style = TextStyle(
                                                    color = Color(0xfff2ff00),
                                                    fontFamily = poppinsFontFamily,
                                                    fontSize = 12.sp
                                                )
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(64.dp))

                                    //Register button
                                    Button(
                                        onClick = {

                                            if(loginRegisterViewModel.validateRegisterForm(
                                                fullName = registerUiState.userName,
                                                email = registerUiState.userEmail,
                                                phoneNumber = registerUiState.userPhone,
                                                password = registerUiState.userPw,
                                                confirmPassword = registerUiState.confPw
                                            )) {
                                                loginRegisterViewModel.createAccount()
                                            } else {
                                                scope.launch {
                                                    registerUiState.errorMessage?.let {
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
                                            text = stringResource(R.string.register),
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                color = Color(0xFF0c4485),
                                                fontFamily = poppinsFontFamily,
                                            )
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
}