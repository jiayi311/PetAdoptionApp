package com.example.ass1.ui.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily

@Composable
fun ProfileScreen(
    onEditInfo: () -> Unit,
    onChangePassword: () -> Unit,
    onSignOut: () -> Unit,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {

    val profileUiState by profileViewModel.uiState.collectAsStateWithLifecycle()
    val nameIcon = painterResource(R.drawable.name_icon)
    val genderIcon = painterResource(R.drawable.gender_icon)
    val mailIcon = painterResource(R.drawable.mail_icon)
    val phoneIcon = painterResource(R.drawable.phone_icon)

    profileViewModel.loadUserProfile()
    profileViewModel.assignUserDataToUiState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF4A7ABF)),
        ) {
            Box(
                modifier = Modifier
                    .weight(1f),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        //  .padding(top = 12.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState()),
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
                            .padding(vertical = 16.dp, horizontal = 32.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xffe7f0f1))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.my_info),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            //User full name
                            InfoRow(nameIcon, stringResource(R.string.name_icon), profileUiState.userName)

                            //User Gender
                            InfoRow(genderIcon, stringResource(R.string.gender_icon), profileViewModel.getGender(profileUiState.userGender))

                            //User Mail Address
                            InfoRow(mailIcon, stringResource(R.string.mail_icon), profileUiState.userMail)

                            //User Phone  Number
                            InfoRow(phoneIcon, stringResource(R.string.phone_icon), profileUiState.userPhone)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = onEditInfo,
                        modifier = Modifier
                            .width(300.dp)
                            .height(48.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                    ) {

                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = stringResource(R.string.edit_information),
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onChangePassword,
                        modifier = Modifier
                            .width(300.dp)
                            .height(48.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                    ) {

                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "Key Icon",
                            tint = Color.White,
                            modifier = Modifier
                                .size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = stringResource(R.string.change_password),
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = stringResource(R.string.sign_out),
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontFamily = poppinsFontFamily,
                            fontSize = 16.sp,
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable(onClick = onSignOut)
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

    }
}

@Composable
fun InfoRow(icon: Painter, description: String, userInfo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Image(
            painter = icon,
            contentDescription = description,
            modifier = Modifier
                .padding(end = 28.dp)
                .size(36.dp)
        )

        Text(
            text = userInfo, //need to be passed
            style = TextStyle(
                color = Color.Black,
                fontFamily = poppinsFontFamily,
                fontSize = 20.sp
            )
        )
    }
}