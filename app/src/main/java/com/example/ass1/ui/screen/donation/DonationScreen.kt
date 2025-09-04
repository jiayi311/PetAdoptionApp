package com.example.ass1.ui.screen.donation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@Composable
fun DonationScreen(
    donationViewModel: DonationViewModel,
    onNavigateToHistory: () -> Unit
) {

    val donateUiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    if (donateUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        donateUiState.isDonate,
        donateUiState.isLoading,
        donateUiState.isDonationSuccessful
    ) {
        // Only proceed if donate was made and loading has finished
        if (donateUiState.isDonate && !donateUiState.isLoading) {
            if (donateUiState.isDonationSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Thank you for your donation!",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                donationViewModel.resetDonationForm()
                onNavigateToHistory()
            } else {
                snackbarHostState.showSnackbar(
                    message = "The donation process is fail. Please try again.",
                    duration = SnackbarDuration.Short
                )
                donationViewModel.resetDonationForm()
            }
        }
    }


    if (isTablet) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color(0xFF4A7ABF))
                    .fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(Color.White)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(14.dp))

                    //Dog Image
                    Image(
                        painter = painterResource(R.drawable.donation_page_dog),
                        contentDescription = "Donation Page Dog",
                        modifier = Modifier
                            .size(140.dp)
                    )

                    //Donation Content
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 28.dp, vertical = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFe7f0f7))
                            .fillMaxWidth()
                    ) {
                        //Donation Title
                        Text(
                            text = stringResource(R.string.donation_title),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = comicSansFontFamily,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(12.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        //Donation Subtitle
                        Text(
                            text = stringResource(R.string.donation_subtitle),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = comicSansFontFamily,
                                fontSize = 24.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(horizontal = 48.dp),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.7.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        DonationContent(
                            donateUiState = donateUiState,
                            donationViewModel = donationViewModel,
                            scope = scope,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .background(color = Color.White)
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(0.4f)
                            .fillMaxHeight()
                            .padding(8.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Image(
                            painter = painterResource(R.drawable.donation_page_dog),
                            contentDescription = "Donation Page Dog",
                            modifier = Modifier
                                .size(112.dp)
                        )
                        //Donation Title
                        Text(
                            text = stringResource(R.string.donation_title),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = comicSansFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(12.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        //Donation Subtitle
                        Text(
                            text = stringResource(R.string.donation_subtitle),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = comicSansFontFamily,
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            modifier = Modifier.padding(horizontal = 48.dp),
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.7.sp
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(0.6f)
                            .fillMaxHeight()
                            .padding(8.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color(0xFFe7f0f7))
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 20.dp)
                        ) {
                            DonationContent(
                                donateUiState = donateUiState,
                                donationViewModel = donationViewModel,
                                scope = scope,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(Color(0xFF4A7ABF))
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Color.White)
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))

                        //Dog Image
                        Image(
                            painter = painterResource(R.drawable.donation_page_dog),
                            contentDescription = "Donation Page Dog",
                            modifier = Modifier
                                .size(112.dp)
                        )

                        //Donation Content
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(horizontal = 28.dp, vertical = 16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFe7f0f7))
                                .fillMaxWidth()
                        ) {
                            //Donation Title
                            Text(
                                text = stringResource(R.string.donation_title),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            //Donation Subtitle
                            Text(
                                text = stringResource(R.string.donation_subtitle),
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = comicSansFontFamily,
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(horizontal = 48.dp),
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.7.sp
                            )

                            DonationContent(
                                donateUiState = donateUiState,
                                donationViewModel = donationViewModel,
                                scope = scope,
                                snackbarHostState = snackbarHostState
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DonationContent(
    donateUiState: DonationUiState,
    donationViewModel: DonationViewModel,
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val buttonPadding = if (isTablet && isLandscape) { 80.dp } else { 20.dp }

    Column(
        modifier = Modifier
            .padding(horizontal = buttonPadding, vertical = 20.dp)
    ) {
        //Donation Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DonationAmountButton(
                5, onClick = { donationViewModel.updateSelectedButton(1) },
                isSelected = donateUiState.selectedButton == 1
            )

            DonationAmountButton(
                10, onClick = { donationViewModel.updateSelectedButton(2) },
                isSelected = donateUiState.selectedButton == 2
            )

            DonationAmountButton(
                20, onClick = { donationViewModel.updateSelectedButton(3) },
                isSelected = donateUiState.selectedButton == 3
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Donation Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            DonationAmountButton(
                30, onClick = { donationViewModel.updateSelectedButton(4) },
                isSelected = donateUiState.selectedButton == 4
            )

            DonationAmountButton(
                40, onClick = { donationViewModel.updateSelectedButton(5) },
                isSelected = donateUiState.selectedButton == 5
            )

            DonationAmountButton(
                50, onClick = { donationViewModel.updateSelectedButton(6) },
                isSelected = donateUiState.selectedButton == 6
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        //Textfield enter amount
        CustomAmountTextField(
            onValueChange = { donationViewModel.updateCustomAmount(it) },
            value = donateUiState.customAmount,
            fieldName = "Custom Amount",
            helpText = "Enter Custom Amount",
            decimalPlaces = 2
        )

        Spacer(modifier = Modifier.height(28.dp))

        DonateNowButton(
            onClick = {
                if (donationViewModel.validateDonationForm()) {
                    donationViewModel.updateAmount()

                    // Use the latest state value directly from the ViewModel
                    val currentAmount =
                        donationViewModel.getCurrentDonateAmount()

                    if (currentAmount != 0.00) {
                        donationViewModel.submitNewDonation()
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Amount should not be 0.",
                                duration = SnackbarDuration.Short
                            )
                            donationViewModel.resetDonationForm()
                        }
                    }

                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = donateUiState.errorMessage!!,
                            duration = SnackbarDuration.Short
                        )
                        donationViewModel.resetDonationForm()
                    }
                }
            },
            customAmount = donateUiState.customAmount,
            selectedButton = donateUiState.selectedButton
        )

        Spacer(modifier = Modifier.height(4.dp))

    }
}

@Composable
fun DonationAmountButton(
    amount: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val buttonHeight = if (isTablet) 56.dp else 32.dp
    val buttonWeight = if (isTablet) 124.dp else 84.dp
    val buttonFontSize = if (isTablet) 20.sp else 12.sp

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xff0c4485) else Color(0xff999999),
            contentColor = Color.White,
        ),

        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .height(buttonHeight)
            .width(buttonWeight)
    ) {
        Text(
            text = "RM $amount",
            style = TextStyle(
                color = Color.White,
                fontFamily = comicSansFontFamily,
                fontSize = buttonFontSize,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CustomAmountTextField(
    value: Double,
    onValueChange: (Double) -> Unit,
    decimalPlaces: Int = 2,
    fieldName: String,
    helpText: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val textFieldMinHeight = if (isTablet) 68.dp else 56.dp
    val helpTextFontSize = if (isTablet) 20.sp else 12.sp
    val fieldNameFontSize = if (isTablet) 24.sp else 12.sp

    // Format the current value to display with specified decimal places
    val formattedValue = remember(value) {
        String.format("%.${decimalPlaces}f", value)
    }

    var textFieldValue by remember { mutableStateOf(formattedValue) }

    // Update the text field when the value changes externally
    LaunchedEffect(value) {
        val newFormatted = String.format("%.${decimalPlaces}f", value)
        if (newFormatted != textFieldValue) {
            textFieldValue = newFormatted
        }
    }

    Column() {
        Text(
            fieldName,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            fontSize = fieldNameFontSize,
            modifier = Modifier.align(Alignment.Start)
        )

        TextField(
            value = textFieldValue,
            onValueChange = { newText ->
                // Only allow digits and a single decimal point
                val filtered = newText.filter { it.isDigit() || it == '.' }

                // Ensure only one decimal point
                val parts = filtered.split(".")
                val formattedText = if (parts.size > 1) {
                    // Limit to specified decimal places
                    val wholePart = parts[0]
                    val decimalPart = parts[1].take(decimalPlaces)
                    "$wholePart.$decimalPart"
                } else {
                    filtered
                }

                textFieldValue = formattedText

                // Convert to double if possible
                formattedText.toDoubleOrNull()?.let { onValueChange(it) }
            },
            label = {
                Text(
                    helpText,
                    fontFamily = comicSansFontFamily,
                    fontSize = helpTextFontSize,
                    color = Color(0xFF8c8787)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal
            ),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),

            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = textFieldMinHeight, max = 112.dp)
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 2.dp) //have a space between text and border

        )
    }
}

@Composable
fun DonateNowButton(
    onClick: () -> Unit,
    selectedButton: Int,
    customAmount: Double
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val buttonHeight = if (isTablet) 52.dp else 40.dp
    val donateNowButtonFontSize = if (isTablet) 28.sp else 20.sp

    val isEnabled = CheckAmountIsEmpty(
        isAnyButton = selectedButton,
        customAmount = customAmount
    )

    Button(
        onClick = { onClick() },
        enabled = isEnabled,
        shape = RoundedCornerShape(8.0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff0e2e6b),
            contentColor = Color.White,
            disabledContainerColor = Color(0xff838383),
            disabledContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
    ) {
        Text(
            "DONATE NOW",
            fontFamily = comicSansFontFamily,
            fontSize = donateNowButtonFontSize,
            fontWeight = FontWeight.Bold,
        )
    }
}

fun CheckAmountIsEmpty(isAnyButton: Int, customAmount: Double): Boolean {
    val isEmpty: Boolean
    if (isAnyButton == 0 && customAmount.equals(0.0)) {
        isEmpty = false
    } else if (isAnyButton != 0 && customAmount > 0) {
        isEmpty = false
    } else {
        isEmpty = true
    }
    return isEmpty
}