package com.example.ass1.ui.screen.booking

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ass1.R
import com.example.ass1.model.Booking
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BookingHistoryScreen(
    bookingViewModel: BookingViewModel
) {

    val bookingUiState by bookingViewModel.uiState.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }
    val bookingList = bookingViewModel.userBookingList
    val reasonList = bookingViewModel.reasonList
    val filteredList = bookingViewModel.userFilterBookings(
        bookingList.value,
        bookingUiState.filterDate,
        bookingUiState.filterReason
    )

    val snackbarHostState = remember { SnackbarHostState() }
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
        bookingUiState.isEdit,
        bookingUiState.isLoading,
        bookingUiState.isEditSuccessful
    ) {
        // Only proceed if edit booking was made and loading has finished
        if (bookingUiState.isEdit && !bookingUiState.isLoading) {
            if (bookingUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Booking cancelled successfully.",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                bookingViewModel.resetBookingForm()
            } else {
                snackbarHostState.showSnackbar(
                    message = "The booking is fail to cancel. Please try again.",
                    duration = SnackbarDuration.Short
                )
                bookingViewModel.resetBookingForm()
            }
        }
    }

    if (isTablet) {

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxWidth()
                        .height(84.dp)
                        .background(Color(0xFF4A7ABF))
                ) {
                    BookingFilterBar(
                        reasonList = reasonList,
                        onFilterChange = { date, reason ->
                            bookingViewModel.updateFilterDate(date)
                            bookingViewModel.updateFilterReason(reason)
                        },
                        bookingViewModel = bookingViewModel
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(top = 68.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(Color.White)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    BookingHistoryList(
                        bookingList = filteredList,
                        onCancel = { booking ->
                            bookingViewModel.getSelectedBooking(booking)
                            bookingViewModel.updateSelectedBookingStatus(3)
                            bookingViewModel.updateBookingToFirebaase()
                            /* update selected booking status, update to firebase */
                        },
                        bookingViewModel = bookingViewModel
                    )
                }
            }
        }
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            if (isLandscape) {
                BookingHistoryLandscape(
                    paddingValues = paddingValues,
                    filterDate = bookingUiState.filterDate,
                    onDateChange = {
                        if (it != null) {
                            bookingViewModel.updateFilterDate(it)
                        }
                    },
                    filterReason = bookingUiState.filterReason,
                    onReasonChange = { bookingViewModel.updateFilterReason(it) },
                    isExpanded = isExpanded,
                    onExpandedChange = { isExpanded = it },
                    reasonList = reasonList,
                    bookingList = filteredList,
                    onCancel = { booking ->
                        bookingViewModel.getSelectedBooking(booking)
                        bookingViewModel.updateSelectedBookingStatus(3)
                        bookingViewModel.updateBookingToFirebaase()
                        /* update selected booking status, update to firebase */
                    }
                    // by default is pending status = 0
                    // admin will set the approve = 1
                    // and reject = 2
                    // user can cancel booking = 3
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxWidth()
                            .height(84.dp)
                            .background(Color(0xFF4A7ABF))
                    ) {
                        BookingFilterBar(
                            reasonList = reasonList,
                            onFilterChange = { date, reason ->
                                bookingViewModel.updateFilterDate(date)
                                bookingViewModel.updateFilterReason(reason)
                            },
                            bookingViewModel = bookingViewModel
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(paddingValues)
                            .padding(top = 68.dp)
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            .background(Color.White)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        BookingHistoryList(
                            bookingList = filteredList,
                            onCancel = { booking ->
                                bookingViewModel.getSelectedBooking(booking)
                                bookingViewModel.updateSelectedBookingStatus(3)
                                bookingViewModel.updateBookingToFirebaase()
                                /* update selected booking status, update to firebase */
                            },
                            bookingViewModel = bookingViewModel
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BookingHistoryLandscape(
    bookingViewModel: BookingViewModel = viewModel(),
    paddingValues: PaddingValues,
    filterDate: Long?,
    onDateChange: (Long?) -> Unit,
    filterReason: String,
    onReasonChange: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    reasonList: List<String>,
    bookingList: List<Booking>,
    onCancel: (Booking) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        BookingFilterBarLandscape(
            selectedText = filterReason,
            onTextChange = onReasonChange,
            selectedDate = filterDate,
            onDateChange = onDateChange,
            isExpanded = isExpanded,
            onExpandedChange = onExpandedChange,
            reasonList = reasonList,
            bookingViewModel = bookingViewModel
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            BookingHistoryList(
                bookingList = bookingList,
                onCancel = onCancel,
                bookingViewModel = bookingViewModel
            )
        }
    }
}

@Composable
fun BookingHistoryCard(
    booking: Booking,
    onCancel: (Booking) -> Unit,
    bookingViewModel: BookingViewModel
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val cardWidth = when {
        isTablet && isLandscape -> 936.dp
        isTablet && !isLandscape -> 576.dp
        !isTablet && isLandscape -> 492.dp
        else -> 388.dp
    }

    val cardSize = if (isTablet) 120.dp else 100.dp
    val cardImageSize = if (isTablet) 32.dp else 20.dp
    val cardFontSize = if (isTablet) 24.sp else 16.sp

    Row(
        modifier = Modifier
            .height(cardSize)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(cardWidth)
                .fillMaxHeight()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(
                    color = bookingViewModel.getBookingStatusColor(booking.status),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.booking_history_reason),
                    contentDescription = null,
                    modifier = Modifier
                        .size(cardImageSize)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = booking.bookingReason,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = bookingViewModel.getBookingStatusTextColor(booking.status)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.booking_history_date),
                    contentDescription = null,
                    modifier = Modifier.size(cardImageSize)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = bookingViewModel.getFormattedDateFromTimestamp(booking.bookDate),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = bookingViewModel.formatTimestampToTime(booking.bookDate),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = { onCancel(booking) },
            enabled = booking.status == 0,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .height(cardSize)
                .width(cardSize)
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            Text(
                text = stringResource(R.string.date_cancel),
                fontSize = cardFontSize,
                fontFamily = comicSansFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Composable
fun BookingHistoryList(
    bookingList: List<Booking>,
    modifier: Modifier = Modifier,
    onCancel: (Booking) -> Unit,
    bookingViewModel: BookingViewModel
) {
    LazyColumn(modifier = modifier.padding(top = 0.dp)) {
        items(bookingList.size) { index ->
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                BookingHistoryCard(
                    booking = bookingList[index],
                    onCancel = onCancel,
                    bookingViewModel = bookingViewModel
                )
            }
        }
    }
}