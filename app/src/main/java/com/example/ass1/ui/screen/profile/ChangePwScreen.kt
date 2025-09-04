package com.example.ass1.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.screen.loginRegister.PasswordRequirement
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChangePwScreen(
    profileViewModel: ProfileViewModel,
    onNavigateToProfile: () -> Unit
) {

    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Log.d("ChangPw",profileUiState.userName)
    Log.d("ChangPw",profileUiState.userMail)
    Log.d("ChangPw",profileUiState.userPhone)
    Log.d("ChangPw",profileUiState.userGender.toString())
    Log.d("ChangPw",profileUiState.userId)

    if (profileUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        profileUiState.isLoading,
        profileUiState.isChangePwSuccessful,
        profileUiState.isChangePw
    ) {
        // Only proceed if reset pw was made and loading has finished
        if (profileUiState.isChangePw && !profileUiState.isLoading) {
            if (profileUiState.isChangePwSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Successfully send the reset password email to your mailbox. Please check it and try to login again.",
                        duration = SnackbarDuration.Short
                    )
                    profileViewModel.resetProfileUiState()
                    delay(500)
                    onNavigateToProfile()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to send the reset password email to your mailbox. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                    profileViewModel.resetProfileUiState()
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(Color(0xFF4A7ABF))
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(horizontal = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(R.drawable.change_password_image),
                    contentDescription = "Change Password Image",
                    modifier = Modifier
                        .size(152.dp),
                    alignment = Alignment.Center

                )
                Spacer(modifier = Modifier.height(32.dp))

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

                    PasswordRequirement(
                        stringResource(R.string.password_requirement_1),
                        color = Color(0xff223545)
                    )
                    PasswordRequirement(
                        stringResource(R.string.password_requirement_2),
                        color = Color(0xff223545)
                    )
                    PasswordRequirement(
                        stringResource(R.string.password_requirement_3),
                        color = Color(0xff223545)
                    )
                    PasswordRequirement(
                        stringResource(R.string.password_requirement_4),
                        color = Color(0xff223545)
                    )
                    PasswordRequirement(
                        stringResource(R.string.password_requirement_5),
                        color = Color(0xff223545)
                    )
                }


                Spacer(modifier = Modifier.height(64.dp))

                //Confirm Change Password Button
                Button(
                    onClick = {
                        profileViewModel.changePassword(profileUiState.userMail)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .width(260.dp)
                        .height(48.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                ) {
                    Text(
                        text = stringResource(R.string.confirm_change),
                        style = TextStyle(
                            fontFamily = poppinsFontFamily,
                            fontSize = 20.sp,
                            color = Color.White
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}