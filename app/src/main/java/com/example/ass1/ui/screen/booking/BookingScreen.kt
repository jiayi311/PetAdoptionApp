package com.example.ass1.ui.screen.booking

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ass1.R
import com.example.ass1.model.BookingDate
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.theme.chewyFontFamily
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BookingScreen(
    bookingViewModel: BookingViewModel,
    onNavigateToHistory: () -> Unit
) {
    val bookingUiState by bookingViewModel.uiState.collectAsStateWithLifecycle()
    val reasonList = bookingViewModel.reasonList
    val bookingDateList = bookingViewModel.bookingDateList
    val bookingTimeList = bookingViewModel.bookingTimeList

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    if (bookingUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        bookingUiState.isBooking,
        bookingUiState.isLoading,
        bookingUiState.isBookingSuccessful
    ) {
        // Only proceed if donate was made and loading has finished
        if (bookingUiState.isBooking && !bookingUiState.isLoading) {
            if (bookingUiState.isBookingSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Booking successfully.",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                bookingViewModel.resetBookingForm()
                onNavigateToHistory()
            } else {
                snackbarHostState.showSnackbar(
                    message = "The booking process is fail. Please try again.",
                    duration = SnackbarDuration.Short
                )
                bookingViewModel.resetBookingForm()
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
                    .fillMaxSize()
                    .background(Color(0xFF4A7ABF))
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(Color.White)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Top
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(R.drawable.booking_dog),
                            contentDescription = "",
                            modifier = Modifier
                                .size(220.dp)
                        )

                        Text(
                            text = stringResource(R.string.booking_title),
                            fontFamily = chewyFontFamily,
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFCC8649),
                            modifier = Modifier
                                .padding(horizontal = 28.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.select_booking_reason),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp, top = 60.dp)
                    )

                    ReasonDropdown(
                        selectedReason = bookingUiState.selectedReason,
                        onReasonSelected = { bookingViewModel.updateBookingReason(it) },
                        reasonList = reasonList,
                        modifier = Modifier
                            .padding(top = 8.dp),
                        isTablet
                    )
                    Spacer(modifier = Modifier.height(56.dp))
                    Text(
                        text = stringResource(R.string.select_date),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    DateSelector(
                        bookingDateList = bookingDateList,
                        selectedDate = bookingUiState.selectedDateIndex,
                        onDateSelected = { bookingViewModel.updateSelectedDateIndex(it) },
                        isTablet
                    )

                    Text(
                        text = stringResource(R.string.select_time),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp, top = 40.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TimeSelector(
                        bookingTimeList = bookingTimeList,
                        selectedTime = bookingUiState.selectedTimeIndex,
                        onTimeSelected = { bookingViewModel.updateSelectedTimeIndex(it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        isTablet
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            if (bookingViewModel.validateBookingForm()) {
                                bookingViewModel.submitNewBooking()
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = bookingUiState.errorMessage!!,
                                        duration = SnackbarDuration.Short
                                    )
                                    bookingViewModel.resetBookingForm()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E2E6B)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .height(88.dp)
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 16.dp, bottom = 16.dp, top = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.confirm_appointment),
                            fontSize = 24.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF4A7ABF))
            ) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(Color.White)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Image(
                        painter = painterResource(R.drawable.booking_dog),
                        contentDescription = "",
                        modifier = Modifier
                            .size(150.dp)
                            .offset(y = -5.dp)
                    )

                    Text(
                        text = stringResource(R.string.booking_title),
                        fontFamily = chewyFontFamily,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFFCC8649),
                        modifier = Modifier
                            .padding(start = 28.dp, end = 28.dp)
                            .offset(y = -20.dp)
                    )
                    Text(
                        text = stringResource(R.string.select_booking_reason),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp)
                            .offset(y = -8.dp)
                    )

                    ReasonDropdown(
                        selectedReason = bookingUiState.selectedReason,
                        onReasonSelected = { bookingViewModel.updateBookingReason(it) },
                        reasonList = reasonList,
                        modifier = Modifier,
                        isTablet
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.select_date),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    DateSelector(
                        bookingDateList = bookingDateList,
                        selectedDate = bookingUiState.selectedDateIndex,
                        onDateSelected = { bookingViewModel.updateSelectedDateIndex(it) },
                        isTablet
                    )

                    Text(
                        text = stringResource(R.string.select_time),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 20.dp)
                            .offset(y = -4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TimeSelector(
                        bookingTimeList = bookingTimeList,
                        selectedTime = bookingUiState.selectedTimeIndex,
                        onTimeSelected = { bookingViewModel.updateSelectedTimeIndex(it) },
                        modifier = Modifier
                            .fillMaxWidth(),
                        isTablet
                    )
                    Button(
                        onClick = {
                            if (bookingViewModel.validateBookingForm()) {
                                bookingViewModel.submitNewBooking()
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = bookingUiState.errorMessage!!,
                                        duration = SnackbarDuration.Short
                                    )
                                    bookingViewModel.resetBookingForm()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E2E6B)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = -8.dp)
                            .padding(start = 20.dp, end = 16.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.confirm_appointment),
                            fontSize = 16.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White

                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReasonDropdown(
    selectedReason: String,
    onReasonSelected: (String) -> Unit,
    reasonList: List<String>,
    modifier: Modifier = Modifier,
    isTablet: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldWidth by remember { mutableStateOf(0f) }
    val density = LocalDensity.current.density

    val textFieldHeight = if (isTablet) 72.dp else 56.dp
    val textFieldSize = if (isTablet) 28.sp else 20.sp
    val textFieldIconSize = if (isTablet) 32.dp else 24.dp

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        TextField(
            value = selectedReason,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    stringResource(R.string.booking_reason),
                    fontSize = textFieldSize,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    tint = Color.Black,
                    modifier = Modifier.size(textFieldIconSize)
                )
            },
            modifier = Modifier
                .menuAnchor()
                .clip(
                    RoundedCornerShape(8.dp)
                )
                .height(textFieldHeight)
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldWidth = coordinates.size.width.toFloat()
                },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFFD5DFEB),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = poppinsFontFamily,
                fontSize = textFieldSize
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFFD5DFEB))
                .width((textFieldWidth / density).dp)
        ) {
            reasonList.forEach { reason ->
                DropdownMenuItem(
                    text = {
                        Text(
                            reason,
                            color = Color.Black
                        )
                    },
                    onClick = {
                        onReasonSelected(reason)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun DateSelector(
    bookingDateList: List<BookingDate>,
    selectedDate: Int,
    onDateSelected: (Int) -> Unit,
    isTablet: Boolean
) {
    if (isTablet) {
        LazyRow(
            modifier = Modifier
                .offset(y = -20.dp)
        ) {
            itemsIndexed(bookingDateList) { index, date ->
                Box(modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .width(80.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (index == selectedDate) Color(0xFF0E2E6B) else
                            Color(0xFFD5DFEB),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDateSelected(index) }
                    .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.day,
                            fontSize = 32.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedDate) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                        Text(
                            text = date.month,
                            fontSize = 20.sp,
                            fontFamily = comicSansFontFamily,
                            color = if (index == selectedDate) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                    }
                }
            }
        }

    } else {

        LazyRow(
            modifier = Modifier
                .offset(y = -20.dp)
        )
        {
            itemsIndexed(bookingDateList) { index, date ->
                Box(modifier = Modifier
                    .padding(start = 20.dp, end = 8.dp)
                    .width(54.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (index == selectedDate) Color(0xFF0E2E6B) else
                            Color(0xFFD5DFEB),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onDateSelected(index) }
                    .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = date.day,
                            fontSize = 20.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedDate) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                        Text(
                            text = date.month,
                            fontSize = 12.sp,
                            fontFamily = comicSansFontFamily,
                            color = if (index == selectedDate) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimeSelector(
    bookingTimeList: List<String>,
    selectedTime: Int,
    onTimeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isTablet: Boolean
) {
    if (isTablet) {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .offset(y = -20.dp)
        ) {
            itemsIndexed(bookingTimeList) { index, time ->
                Box(modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .width(108.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (index == selectedTime) Color(0xFF0E2E6B) else
                            Color(0xFFD5DFEB),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTimeSelected(index) }
                    .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = time,
                            fontSize = 28.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedTime) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                    }
                }
            }
        }
    } else {
        LazyRow(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .offset(y = -20.dp)
        ) {
            itemsIndexed(bookingTimeList) { index, time ->
                Box(modifier = Modifier
                    .padding(start = 20.dp, end = 8.dp)
                    .width(73.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (index == selectedTime) Color(0xFF0E2E6B) else
                            Color(0xFFD5DFEB),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onTimeSelected(index) }
                    .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = time,
                            fontSize = 16.sp,
                            fontFamily = comicSansFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedTime) colorResource(R.color.white) else
                                colorResource(R.color.black)
                        )
                    }
                }
            }
        }
    }
}