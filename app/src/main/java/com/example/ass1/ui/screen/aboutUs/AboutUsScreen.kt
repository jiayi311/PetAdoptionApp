package com.example.ass1.ui.screen.aboutUs

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AboutUsScreen(
    onNavigateBack: () -> Unit,
    onClickHome: () -> Unit,
    onClickProfile: () -> Unit,
    onClickCommunity: () -> Unit,
    onNavigateToHome : () -> Unit,
    onNavigateToCommunity : () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    aboutUsViewModel: AboutUsViewModel
) {
    val aboutUsUiState by aboutUsViewModel.uiState.collectAsStateWithLifecycle()
    var selectedNavItem by remember { mutableStateOf(0) }
    val context = LocalContext.current

    var showContactDialog by remember { mutableStateOf(false) }

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    if (isTablet) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            SideBar(
                currentScreen = "About Us",
                onPetListClick = onNavigateToPetList,
                onCommunityClick = onNavigateToCommunity,
                onBookingListClick = onNavigateToBookingList,
                onReportClick = onNavigateToReport,
                onDonationClick = onNavigateToDonation,
                onBackHome = onNavigateToHome,
                onProfileClick = onNavigateToProfile,
                onAboutUsClick = {},
                isTablet = isTablet,
                isLandscape = isLandscape
            )

            Scaffold(
                topBar = {
                    MyTopBar(
                        "About Us",
                        modifier = Modifier,
                        onNavigateBack = onNavigateBack,
                        isTablet = isTablet,
                        isLandscape = isLandscape
                    )
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF4A7ABF))
                        .padding(paddingValues)
                ) {
                    //About Us Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Color.White)
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(44.dp)
                    ) {

                        //Text & Image
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //Text
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {

                                // "Welcome!"
                                Text(
                                    text = stringResource(R.string.welcome),
                                    style = TextStyle(
                                        color = Color(0xff0e2e6b),
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 36.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                // "Furever Friends"
                                Text(
                                    text = stringResource(R.string.furever_friends),
                                    style = TextStyle(
                                        color = Color(0xff0e2e6b),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 36.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )
                            }

                            Image(
                                painter = painterResource(R.drawable.about_us_picture),
                                contentDescription = "About Us Picture",
                                modifier = Modifier
                                    .size(152.dp)
                            )
                        }

                        Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 10.dp))

                        //Mission Statement
                        OurMissionStatement(isTablet)

                        Spacer(modifier = Modifier.height(8.dp))

                        //Mission Description
                        MissionStatementDescription(
                            aboutUsViewModel.aboutUsData.value.mission,
                            isTablet
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //Operation hours
                        OperationHours(aboutUsViewModel.aboutUsData.value.operationHour, isTablet)

                        Spacer(modifier = Modifier.height(12.dp))

                        //Phone Number
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.data = Uri.parse("tel:+60165807275")
                                    context.startActivity(intent)
                                }
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            //Black phone icon
                            Image(
                                painter = painterResource(R.drawable.black_phone_icon),
                                contentDescription = "Black Phone Icon",
                                modifier = Modifier
                                    .size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            //Phone Number
                            Text(
                                text = ": ${aboutUsViewModel.aboutUsData.value.phoneNumber}",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontFamily = comicSansFontFamily
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        //Mail Address
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:fureverfriends@gmail.com")
                                    }
                                    context.startActivity(intent)
                                }
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))

                            //Black mail icon
                            Image(
                                painter = painterResource(R.drawable.black_mail_icon),
                                contentDescription = "Black Mail Icon",
                                modifier = Modifier
                                    .size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            //Mail Address Text
                            Text(
                                text = ": ${aboutUsViewModel.aboutUsData.value.email}",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 24.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        //Address
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))

                            //Black mail icon
                            Image(
                                painter = painterResource(R.drawable.black_location_icon),
                                contentDescription = "Black Location Icon",
                                modifier = Modifier
                                    .size(40.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = ": ",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 24.sp
                                )
                            )

                            //Address Text
                            Text(
                                text = aboutUsViewModel.aboutUsData.value.address,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 24.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(64.dp))

                        //Contact Us Button
                        Button(
                            onClick = { aboutUsViewModel.updateShowDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0e2e6b)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(R.string.contact_us),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 28.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                if (aboutUsUiState.showContactDialog) {
                    ContactDialog(
                        onDismissRequest = { aboutUsViewModel.updateShowDialog(false) },
                        onDial = {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:+60165807275")
                            context.startActivity(intent)
                            aboutUsViewModel.updateShowDialog(false)
                        },
                        onEmail = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:fureverfriends@gmail.com")
                            }
                            context.startActivity(intent)
                            aboutUsViewModel.updateShowDialog(false)
                        },
                        isTablet
                    )
                }

            }
        }
    } else {
        if (isLandscape) {
            Row {
                SideBar(
                    currentScreen = "About Us",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = onNavigateToDonation,
                    onBackHome = onNavigateToHome,
                    onProfileClick = onNavigateToProfile,
                    onAboutUsClick = {},
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "About Us",
                            modifier = Modifier,
                            onNavigateBack = onNavigateBack,
                            isTablet = isTablet,
                            isLandscape = isLandscape
                        )
                    }

                ) { paddingValues ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF4A7ABF))
                            .padding(paddingValues)
                    ) {
                        //About Us Content
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(Color.White)
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(36.dp)
                        ) {

                            //Text & Image
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                //Text
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {

                                    // "Welcome!"
                                    Text(
                                        text = stringResource(R.string.welcome),
                                        style = TextStyle(
                                            color = Color(0xff0e2e6b),
                                            fontFamily = comicSansFontFamily,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    // "Furever Friends"
                                    Text(
                                        text = stringResource(R.string.furever_friends),
                                        style = TextStyle(
                                            color = Color(0xff0e2e6b),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 28.sp,
                                            fontFamily = comicSansFontFamily
                                        )
                                    )
                                }

                                Image(
                                    painter = painterResource(R.drawable.about_us_picture),
                                    contentDescription = "About Us Picture",
                                    modifier = Modifier
                                        .size(152.dp)
                                )
                            }

                            Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 10.dp))

                            //Mission Statement
                            OurMissionStatement(isTablet)

                            Spacer(modifier = Modifier.height(8.dp))

                            //Mission Description
                            MissionStatementDescription(
                                aboutUsViewModel.aboutUsData.value.mission,
                                isTablet
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            //Operation hours
                            OperationHours(
                                aboutUsViewModel.aboutUsData.value.operationHour,
                                isTablet
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            //Phone Number
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_DIAL)
                                        intent.data = Uri.parse("tel:+60165807275")
                                        context.startActivity(intent)
                                    }
                            ) {
                                Spacer(modifier = Modifier.width(4.dp))
                                //Black phone icon
                                Image(
                                    painter = painterResource(R.drawable.black_phone_icon),
                                    contentDescription = "Black Phone Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                //Phone Number
                                Text(
                                    text = ": ${aboutUsViewModel.aboutUsData.value.phoneNumber}",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            //Mail Address
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:fureverfriends@gmail.com")
                                        }
                                        context.startActivity(intent)
                                    }
                            ) {
                                Spacer(modifier = Modifier.width(4.dp))

                                //Black mail icon
                                Image(
                                    painter = painterResource(R.drawable.black_mail_icon),
                                    contentDescription = "Black Mail Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                //Mail Address Text
                                Text(
                                    text = ": ${aboutUsViewModel.aboutUsData.value.email}",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 16.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            //Address
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                            ) {
                                Spacer(modifier = Modifier.width(4.dp))

                                //Black mail icon
                                Image(
                                    painter = painterResource(R.drawable.black_location_icon),
                                    contentDescription = "Black Location Icon",
                                    modifier = Modifier
                                        .size(32.dp)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = ": ",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 16.sp
                                    )
                                )

                                //Address Text
                                Text(
                                    text = aboutUsViewModel.aboutUsData.value.address,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 16.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(64.dp))

                            //Contact Us Button
                            Button(
                                onClick = { aboutUsViewModel.updateShowDialog(true) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xff0e2e6b
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = stringResource(R.string.contact_us),
                                    style = TextStyle(
                                        color = Color.White,
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 20.sp
                                    ),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }

                    if (aboutUsUiState.showContactDialog) {
                        ContactDialog(
                            onDismissRequest = { aboutUsViewModel.updateShowDialog(false) },
                            onDial = {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:+60165807275")
                                context.startActivity(intent)
                                aboutUsViewModel.updateShowDialog(false)
                            },
                            onEmail = {
                                val intent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:fureverfriends@gmail.com")
                                }
                                context.startActivity(intent)
                                aboutUsViewModel.updateShowDialog(false)
                            },
                            isTablet
                        )
                    }

                }
            }
        } else {
            Scaffold(
                topBar = {
                    MyTopBar(
                        "About Us",
                        modifier = Modifier,
                        onNavigateBack = onNavigateBack,
                        isTablet = isTablet,
                        isLandscape = isLandscape
                    )
                },
                bottomBar = {
                    MyBottomBar(
                        selectedItem = selectedNavItem,
                        onClickHome = onClickHome,
                        onClickCommunity = onClickCommunity,
                        onClickProfile = onClickProfile
                    )
                }

            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF4A7ABF))
                        .padding(paddingValues)
                ) {
                    //About Us Content
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Color.White)
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(36.dp)
                    ) {

                        //Text & Image
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            //Text
                            Column(
                                horizontalAlignment = Alignment.Start
                            ) {

                                // "Welcome!"
                                Text(
                                    text = stringResource(R.string.welcome),
                                    style = TextStyle(
                                        color = Color(0xff0e2e6b),
                                        fontFamily = comicSansFontFamily,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                // "Furever Friends"
                                Text(
                                    text = stringResource(R.string.furever_friends),
                                    style = TextStyle(
                                        color = Color(0xff0e2e6b),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )
                            }

                            Image(
                                painter = painterResource(R.drawable.about_us_picture),
                                contentDescription = "About Us Picture",
                                modifier = Modifier
                                    .size(152.dp)
                            )
                        }

                        Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 10.dp))

                        //Mission Statement
                        OurMissionStatement(isTablet)

                        Spacer(modifier = Modifier.height(8.dp))

                        //Mission Description
                        MissionStatementDescription(
                            aboutUsViewModel.aboutUsData.value.mission,
                            isTablet
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        //Operation hours
                        OperationHours(aboutUsViewModel.aboutUsData.value.operationHour, isTablet)

                        Spacer(modifier = Modifier.height(12.dp))

                        //Phone Number
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.data = Uri.parse("tel:+60165807275")
                                    context.startActivity(intent)
                                }
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))
                            //Black phone icon
                            Image(
                                painter = painterResource(R.drawable.black_phone_icon),
                                contentDescription = "Black Phone Icon",
                                modifier = Modifier
                                    .size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            //Phone Number
                            Text(
                                text = ": ${aboutUsViewModel.aboutUsData.value.phoneNumber}",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontFamily = comicSansFontFamily
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        //Mail Address
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:fureverfriends@gmail.com")
                                    }
                                    context.startActivity(intent)
                                }
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))

                            //Black mail icon
                            Image(
                                painter = painterResource(R.drawable.black_mail_icon),
                                contentDescription = "Black Mail Icon",
                                modifier = Modifier
                                    .size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            //Mail Address Text
                            Text(
                                text = ": ${aboutUsViewModel.aboutUsData.value.email}",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        //Address
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                        ) {
                            Spacer(modifier = Modifier.width(4.dp))

                            //Black mail icon
                            Image(
                                painter = painterResource(R.drawable.black_location_icon),
                                contentDescription = "Black Location Icon",
                                modifier = Modifier
                                    .size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = ": ",
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 16.sp
                                )
                            )

                            //Address Text
                            Text(
                                text = aboutUsViewModel.aboutUsData.value.address,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 16.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(64.dp))

                        //Contact Us Button
                        Button(
                            onClick = { aboutUsViewModel.updateShowDialog(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0e2e6b)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                text = stringResource(R.string.contact_us),
                                style = TextStyle(
                                    color = Color.White,
                                    fontFamily = poppinsFontFamily,
                                    fontSize = 20.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                if (aboutUsUiState.showContactDialog) {
                    ContactDialog(
                        onDismissRequest = { aboutUsViewModel.updateShowDialog(false) },
                        onDial = {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:+60165807275")
                            context.startActivity(intent)
                            aboutUsViewModel.updateShowDialog(false)
                        },
                        onEmail = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:fureverfriends@gmail.com")
                            }
                            context.startActivity(intent)
                            aboutUsViewModel.updateShowDialog(false)
                        },
                        isTablet
                    )
                }

            }
        }
    }

}

@Composable
fun ContactDialog(
    onDismissRequest: () -> Unit,
    onDial: () -> Unit,
    onEmail: () -> Unit,
    isTablet: Boolean
) {
    if (isTablet) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.contact_us),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = poppinsFontFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.how_would_you_like_to_contact_us),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = comicSansFontFamily,
                            fontSize = 24.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Close",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 24.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onDismissRequest() }
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = "Call Us",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 24.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onDial() }
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            Text(
                                text = "Email Us",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 24.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onEmail() }
                            )
                        }
                    }
                }
            }
        }
    } else {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.contact_us),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = poppinsFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.how_would_you_like_to_contact_us),
                        style = TextStyle(
                            color = Color.Black,
                            fontFamily = comicSansFontFamily,
                            fontSize = 16.sp
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "Close",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onDismissRequest() }
                            )
                        }

                        Spacer(modifier = Modifier.width(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                        ) {
                            Text(
                                text = "Call Us",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onDial() }
                            )

                            Spacer(modifier = Modifier.width(20.dp))

                            Text(
                                text = "Email Us",
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                ),
                                modifier = Modifier
                                    .clickable { onEmail() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OurMissionStatement(
    isTablet: Boolean
) {
    if (isTablet) {
        Row() {
            // 'Our Mission' text
            Text(
                text = stringResource(R.string.mission_statement),
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontFamily = poppinsFontFamily,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Mission Icon
            Image(
                painter = painterResource(R.drawable.mission_icon),
                contentDescription = "Mission Icon",
                modifier = Modifier
                    .size(42.dp)
            )
        }
    } else {
        Row() {
            // 'Our Mission' text
            Text(
                text = stringResource(R.string.mission_statement),
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontFamily = poppinsFontFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Mission Icon
            Image(
                painter = painterResource(R.drawable.mission_icon),
                contentDescription = "Mission Icon",
                modifier = Modifier
                    .size(32.dp)
            )
        }
    }
}

@Composable
fun MissionStatementDescription(
    mission: String,
    isTablet: Boolean
) {
    if (isTablet) {
        Text(
            text = mission,
            style = TextStyle(
                color = Color.Black,
                fontFamily = comicSansFontFamily,
                fontSize = 28.sp,
            ),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(12.dp)
        )
    } else {
        Text(
            text = mission,
            style = TextStyle(
                color = Color.Black,
                fontFamily = comicSansFontFamily,
                fontSize = 20.sp,
            ),
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(12.dp)
        )
    }
}

@Composable
fun OperationHours(
    oh: String,
    isTablet: Boolean
) {
    if (isTablet) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Operation hours Icon
            Image(
                painter = painterResource(R.drawable.operation_hours_icon),
                contentDescription = "Operation hours",
                modifier = Modifier
                    .size(52.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))
            //Operation hours text
            Text(
                text = ": $oh",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontFamily = comicSansFontFamily
                )
            )
        }
    } else {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Operation hours Icon
            Image(
                painter = painterResource(R.drawable.operation_hours_icon),
                contentDescription = "Operation hours",
                modifier = Modifier
                    .size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))
            //Operation hours text
            Text(
                text = ": $oh",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = comicSansFontFamily
                )
            )
        }
    }
}