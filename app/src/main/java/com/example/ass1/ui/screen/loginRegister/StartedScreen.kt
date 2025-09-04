package com.example.ass1.ui.screen.loginRegister

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ass1.R
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.theme.poppinsFontFamily

@Composable
fun StartPageScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    SetSystemNavColor(0xffe7f4ff, 0xffe7f4ff)

    if (isTablet) {
        if(isLandscape) {
            Box(
                modifier = Modifier
                    .background(Color(0xffe7f4ff))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.start_page),
                            contentDescription = "Friends",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .size(620.dp)
                                .fillMaxHeight(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.find_your_next_best_friend),
                            style = TextStyle(
                                color = Color(0xFF0c4485),
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFontFamily
                            )
                        )

                        Spacer(modifier = Modifier.height(36.dp))

                        Text(
                            text = stringResource(R.string.start_page_description),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 32.sp,
                                fontFamily = poppinsFontFamily,
                            )
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = onGetStartedClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(68.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(32.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                        ) {
                            Image(
                                painter = painterResource(R.drawable.white_paw),
                                contentDescription = "White Paw",
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.get_started),
                                style = TextStyle(
                                    fontSize = 32.sp,
                                    color = Color.White,
                                    fontFamily = poppinsFontFamily,
                                )
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .background(Color(0xffe7f4ff))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.find_your_next_best_friend),
                        style = TextStyle(
                            color = Color(0xFF0c4485),
                            fontSize = 52.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.start_page_description),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 32.sp,
                            fontFamily = poppinsFontFamily,
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.start_page),
                            contentDescription = "Friends",
                            modifier = Modifier
                                .size(620.dp)
                                .padding(vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = onGetStartedClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.white_paw),
                            contentDescription = "White Paw",
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.get_started),
                            style = TextStyle(
                                fontSize = 32.sp,
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                            )
                        )
                    }
                }
            }
        }
    } else {
        if(isLandscape) {
            Box(
                modifier = Modifier
                    .background(Color(0xffe7f4ff))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.start_page),
                            contentDescription = "Friends",
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .size(280.dp)
                                .fillMaxHeight(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.find_your_next_best_friend),
                            style = TextStyle(
                                color = Color(0xFF0c4485),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFontFamily
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.start_page_description),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = poppinsFontFamily,
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onGetStartedClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(32.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                        ) {
                            Image(
                                painter = painterResource(R.drawable.white_paw),
                                contentDescription = "White Paw",
                                alignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxHeight()
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.get_started),
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    fontFamily = poppinsFontFamily,
                                )
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .background(Color(0xffe7f4ff))
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        text = stringResource(R.string.find_your_next_best_friend),
                        style = TextStyle(
                            color = Color(0xFF0c4485),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.start_page_description),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.start_page),
                            contentDescription = "Friends",
                            modifier = Modifier
                                .size(520.dp)
                                .padding(vertical = 16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onGetStartedClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(32.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xff0e2e6b))
                    ) {
                        Image(
                            painter = painterResource(R.drawable.white_paw),
                            contentDescription = "White Paw",
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(R.string.get_started),
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.White,
                                fontFamily = poppinsFontFamily,
                            )
                        )
                    }
                }
            }
        }
    }
}