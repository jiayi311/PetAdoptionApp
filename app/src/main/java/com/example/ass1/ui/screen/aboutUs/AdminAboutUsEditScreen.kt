package com.example.ass1.ui.screen.aboutUs

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminAboutUsEditScreen(
    modifier: Modifier = Modifier,
    aboutUsViewModel: AboutUsViewModel,
    onNavigateToAboutUs: () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToCommunity: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
) {

    val aboutUsUiState by aboutUsViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    if (aboutUsUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        aboutUsUiState.isEdit,
        aboutUsUiState.isLoading,
        aboutUsUiState.isEditSuccessful
    ) {
        // Only proceed if register was made and loading has finished
        if (aboutUsUiState.isEdit && !aboutUsUiState.isLoading) {
            if (aboutUsUiState.isEditSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Update data successful",
                        duration = SnackbarDuration.Short
                    )

                    delay(200)
                    onNavigateToAboutUs()
                    aboutUsViewModel.resetEditForm()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to update data. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                   aboutUsViewModel.resetEditForm()
                }
            }
        }
    }

    if(isTablet) {
        Row{
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0,0,0,0)
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                            }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF4A7ABF))
                        //.padding(WindowInsets.navigationBars.asPaddingValues())
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
                            EditMissionDescription(
                                newMission = aboutUsUiState.mission,
                                onTextChange = {
                                    aboutUsViewModel.updateMission(it)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

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
                                    text = ": ",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )

                                EditableField(
                                    value = aboutUsUiState.operationHour,
                                    onValueChange = {
                                        aboutUsViewModel.updateOperationHour(it)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                            }

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
                                EditableField(
                                    value = aboutUsUiState.address,
                                    onValueChange = {
                                        aboutUsViewModel.updateAddress(it)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(64.dp))

                            //Save Changes Button
                            Button(
                                onClick = {

                                    if(aboutUsViewModel.validateEditForm()) {
                                        aboutUsViewModel.updateEditData()
                                    } else {
                                        scope.launch {
                                            aboutUsUiState.errorMessage?.let {
                                                snackbarHostState.showSnackbar(
                                                    message = it,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0e2e6b)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = stringResource(R.string.save_changes),
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
                }
            }
        }
    } else {
        if(isLandscape) {
            Row{
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    containerColor = Color.Transparent,
                    contentWindowInsets = WindowInsets(0,0,0,0)
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    focusManager.clearFocus()
                                }
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFF4A7ABF))
                            //.padding(WindowInsets.navigationBars.asPaddingValues())
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
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    //Text
                                    Column(
                                        horizontalAlignment = Alignment.Start
                                    ) {

                                        // "Welcome!"
                                        Text(
                                            text = stringResource(R.string.welcome),
                                            style = TextStyle(
                                                color = Color.White,
                                                fontFamily = comicSansFontFamily,
                                                fontSize = 28.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )

                                        // "Furever Friends"
                                        Text(
                                            text = stringResource(R.string.furever_friends),
                                            style = TextStyle(
                                                color = Color.White,
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
                                            .fillMaxWidth()
                                            .size(152.dp)
                                    )
                                }

                                //Mission Statement
                                OurMissionStatement(isTablet)

                                Spacer(modifier = Modifier.height(8.dp))

                                //Mission Description
                                EditMissionDescription(
                                    newMission = aboutUsUiState.mission,
                                    onTextChange = {
                                        aboutUsViewModel.updateMission(it)
                                    }
                                )

                                Spacer(modifier = Modifier.height(24.dp))

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
                                        text = ": ",
                                        style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 16.sp,
                                            fontFamily = comicSansFontFamily
                                        )
                                    )

                                    EditableField(
                                        value = aboutUsUiState.operationHour,
                                        onValueChange = {
                                            aboutUsViewModel.updateOperationHour(it)
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                    )
                                }

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
                                    EditableField(
                                        value = aboutUsUiState.address,
                                        onValueChange = {
                                            aboutUsViewModel.updateAddress(it)
                                        },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(64.dp))

                                //Save Changes Button
                                Button(
                                    onClick = {

                                        if(aboutUsViewModel.validateEditForm()) {
                                            aboutUsViewModel.updateEditData()
                                        } else {
                                            scope.launch {
                                                aboutUsUiState.errorMessage?.let {
                                                    snackbarHostState.showSnackbar(
                                                        message = it,
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0e2e6b)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                ) {
                                    Text(
                                        text = stringResource(R.string.save_changes),
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
                    }
                }
            }
        } else {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                containerColor = Color.Transparent,
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                focusManager.clearFocus()
                            }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF4A7ABF))
                        //.padding(WindowInsets.navigationBars.asPaddingValues())
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
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                //Text
                                Column(
                                    horizontalAlignment = Alignment.Start
                                ) {

                                    // "Welcome!"
                                    Text(
                                        text = stringResource(R.string.welcome),
                                        style = TextStyle(
                                            color = Color.White,
                                            fontFamily = comicSansFontFamily,
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )

                                    // "Furever Friends"
                                    Text(
                                        text = stringResource(R.string.furever_friends),
                                        style = TextStyle(
                                            color = Color.White,
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
                                        .fillMaxWidth()
                                        .size(152.dp)
                                )
                            }

                            //Mission Statement
                            OurMissionStatement(isTablet)

                            Spacer(modifier = Modifier.height(8.dp))

                            //Mission Description
                            EditMissionDescription(
                                newMission = aboutUsUiState.mission,
                                onTextChange = {
                                    aboutUsViewModel.updateMission(it)
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

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
                                    text = ": ",
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 16.sp,
                                        fontFamily = comicSansFontFamily
                                    )
                                )

                                EditableField(
                                    value = aboutUsUiState.operationHour,
                                    onValueChange = {
                                        aboutUsViewModel.updateOperationHour(it)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                                )
                            }

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
                                EditableField(
                                    value = aboutUsUiState.address,
                                    onValueChange = {
                                        aboutUsViewModel.updateAddress(it)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(64.dp))

                            //Save Changes Button
                            Button(
                                onClick = {

                                    if (aboutUsViewModel.validateEditForm()) {
                                        aboutUsViewModel.updateEditData()
                                    } else {
                                        scope.launch {
                                            aboutUsUiState.errorMessage?.let {
                                                snackbarHostState.showSnackbar(
                                                    message = it,
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(
                                        0xff0e2e6b
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = stringResource(R.string.save_changes),
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
                }
            }
        }
    }
}

@Composable
fun EditMissionDescription(
    newMission: String,
    onTextChange: (String) -> Unit
) {
    TextField(
        value = newMission,
        onValueChange = onTextChange,
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = comicSansFontFamily,
            fontSize = 20.sp,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {},
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xffdee9ff),
            unfocusedContainerColor = Color(0xffedf3ff),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            cursorColor = Color(0xff0e2e6b),
            selectionColors = TextSelectionColors(
                handleColor = Color(0xff0e2e6b),
                backgroundColor = Color(0xff0e2e6b).copy(0.2f)
            )
        )
    )
}

@Composable
fun EditableField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = Color.Black,
            fontFamily = comicSansFontFamily,
            fontSize = 16.sp,
        ),
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xffdee9ff),
            unfocusedContainerColor = Color(0xffedf3ff),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color(0xff0e2e6b),
            selectionColors = TextSelectionColors(
                handleColor = Color(0xff0e2e6b),
                backgroundColor = Color(0xff0e2e6b).copy(alpha = 0.2f)
            )
        ),
        keyboardOptions = keyboardOptions
    )
}