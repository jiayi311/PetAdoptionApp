package com.example.ass1.ui.screen.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun EditProfileScreen(
    onNavigateToProfileScreen: () -> Unit,
    profileViewModel: ProfileViewModel
) {
    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        profileUiState.isEdit,
        profileUiState.isLoading,
        profileUiState.isEditSuccessful
    ) {
        // Only proceed if donate was made and loading has finished
        if (profileUiState.isEdit && !profileUiState.isLoading) {
            if (profileUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Your information has updated!",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                profileViewModel.resetProfileUiState()
                onNavigateToProfileScreen()
            } else {
                snackbarHostState.showSnackbar(
                    message = "Your information is fail to update. Please try again.",
                    duration = SnackbarDuration.Short
                )
                profileViewModel.resetProfileUiState()
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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                //User Profile Picture
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {

                    if(profileUiState.userGender == 0) {
                        Image(
                            painter = painterResource(R.drawable.unknown_profile_picture),
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier
                                .clip(shape = CircleShape) //let user picture become circle
                                .size(140.dp),
                            contentScale = ContentScale.Fit
                        )
                    } else if (profileUiState.userGender == 1) {
                        Image(
                            painter = painterResource(R.drawable.male_profile_picture),
                            contentDescription = "Male Profile Picture",
                            modifier = Modifier
                                .clip(shape = CircleShape) //let user picture become circle
                                .size(140.dp),
                            contentScale = ContentScale.Fit
                        )
                    }else if (profileUiState.userGender == 2) {
                        Image(
                            painter = painterResource(R.drawable.female_profile_picture),
                            contentDescription = "Female Profile Picture",
                            modifier = Modifier
                                .clip(shape = CircleShape) //let user picture become circle
                                .size(140.dp),
                            contentScale = ContentScale.Fit
                        )
                    }else {
                        Image(
                            painter = painterResource(R.drawable.unknown_profile_picture),
                            contentDescription = "Default Profile Picture",
                            modifier = Modifier
                                .clip(shape = CircleShape) //let user picture become circle
                                .size(140.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                //User Information
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 28.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF4e84cc))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        //Full Name Field
                        Text(
                            text = stringResource(R.string.full_name),
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = profileUiState.userName,
                            onValueChange = { profileViewModel.updateUserName(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(8.dp),
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
                                    text = profileViewModel.currentUser.value.userName,
                                    style = TextStyle(
                                        color = Color.Gray
                                    )
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        //Gender Field
                        GenderSelector(
                            selectedGender = profileUiState.userGender,
                            onGenderSelected = { profileViewModel.updateUserGender(it)}
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        //Phone Number Field
                        Text(
                            text = stringResource(R.string.phone_number),
                            style = TextStyle(
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.padding(start = 20.dp)
                        ) {
                            Text(
                                text = " • Format: ",
                                style = TextStyle(
                                    color = Color(0xff223545),
                                    fontSize = 12.sp,
                                    fontFamily = comicSansFontFamily,
                                )
                            )
                            Column( ) {
                                Text(
                                    text = stringResource(R.string.phone_number_format1),
                                    style = TextStyle(
                                        color = Color(0xff223545),
                                        fontSize = 12.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )
                                Text(
                                    text = stringResource(R.string.phone_number_format2),
                                    style = TextStyle(
                                        color = Color(0xff223545),
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        TextField(
                            value = profileUiState.userPhoneUi,
                            onValueChange = { textFieldValue ->
                                profileViewModel.updateUserPhone(textFieldValue.text)
                                profileViewModel.phoneValidation(textFieldValue.text)},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            shape = RoundedCornerShape(8.dp),
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
                                    text = profileViewModel.currentUser.value.userPhone,
                                    style = TextStyle(
                                        color = Color.Gray
                                    )
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val itemWidth = maxWidth / 2

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        //Cancel Button
                        OutlinedButton(
                            onClick = {
                                profileViewModel.resetProfileUiState()
                                onNavigateToProfileScreen()
                                      },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            //.width(itemWidth),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xff0e2e6b)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Cancel",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        //Save Button
                        Button(
                            onClick = {
                                if(profileViewModel.validateEditForm()) {
                                    profileViewModel.updateProfile()
                                }else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = profileUiState.errorMessage!!,
                                            duration = SnackbarDuration.Short
                                        )
                                        profileViewModel.resetProfileUiState()
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp),
                            //.width(itemWidth),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xff0e2e6b),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Save Changes",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GenderSelector(
    selectedGender: Int,
    onGenderSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val genderOptions = listOf(
        0 to "Unknown",
        1 to "Male",
        2 to "Female"
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.gender),
            style = TextStyle(
                color = Color.White,
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        genderOptions.forEach { (value, displayText) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onGenderSelected(value) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = value == selectedGender,
                    onClick = { onGenderSelected(value) }
                )

                Text(
                    text = displayText,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = poppinsFontFamily
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Text(
            text = stringResource(R.string.change_gender_message),
            style = TextStyle(
                color = Color(0xfff2ff00),
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily
            ),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}