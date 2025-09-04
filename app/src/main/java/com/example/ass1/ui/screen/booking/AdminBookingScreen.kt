package com.example.ass1.ui.screen.booking

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ass1.R
import com.example.ass1.model.AdminBookingHistory
import com.example.ass1.model.Booking
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi", "StateFlowValueCalledInComposition")
@Composable
fun AdminBookingScreen(
    bookingViewModel: BookingViewModel
) {

    bookingViewModel.bookingHistoryList()
    val bookingUiState by bookingViewModel.uiState.collectAsStateWithLifecycle()
    var isExpanded by remember { mutableStateOf(false) }
    //val bookingList = bookingViewModel.bookingList
    val bookingList = bookingViewModel.adminBookingList
    val reasonList = bookingViewModel.reasonList
    val filteredList = bookingViewModel.filterBookings(
        bookingList.value,
        bookingUiState.filterDate,
        bookingUiState.filterReason
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val onExpandedChange: (Boolean) -> Unit = { isExpanded = it }
    val twoMonthsDates = getTwoMonthsDateRange()

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

//    if (bookingUiState.isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//        return
//    }

    LaunchedEffect(
        bookingUiState.isEdit,
        bookingUiState.isLoading,
        bookingUiState.isEditSuccessful
    ) {
        // Only proceed if edit booking was made and loading has finished
        if (bookingUiState.isEdit && !bookingUiState.isLoading) {
            if (bookingUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Update booking successfully.",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                bookingViewModel.resetBookingForm()
            } else {
                snackbarHostState.showSnackbar(
                    message = "The booking is fail to update. Please try again.",
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
                        reasonList = bookingViewModel.reasonList,
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
                    AppointmentList(
                        bookingList = filteredList,
                        onStatusChange = { booking, newStatus ->
                            bookingViewModel.getSelectedBooking(booking)
                            bookingViewModel.updateSelectedBookingStatus(newStatus)
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
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(color = Color.White)
                ) {
                    BookingFilterBarLandscape(
                        selectedText = bookingUiState.filterReason,
                        onTextChange = { bookingViewModel.updateFilterReason(it) },
                        selectedDate = bookingUiState.filterDate,
                        onDateChange = {
                            if (it != null) {
                                bookingViewModel.updateFilterDate(it)
                            }
                        },
                        isExpanded = isExpanded,
                        onExpandedChange = onExpandedChange,
                        reasonList = reasonList,
                        bookingViewModel = bookingViewModel
                    )
                    AppointmentContent(
                        bookings = filteredList,
                        twoMonthsDates = twoMonthsDates,
                        onStatusChange = { booking, newStatus ->
                            bookingViewModel.getSelectedBooking(booking)
                            bookingViewModel.updateSelectedBookingStatus(newStatus)
                            bookingViewModel.updateBookingToFirebaase()
                            /* update selected booking status, update to firebase */
                        },
                        bookingViewModel = bookingViewModel
                    )
                }
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
                            reasonList = bookingViewModel.reasonList,
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
//                    BookingListByDate(
//                        allDates = twoMonthsDates,
//                        bookings = filteredList,
//                        onStatusChange = { booking, newStatus ->
//                            bookingViewModel.getSelectedBooking(booking)
//                            bookingViewModel.updateSelectedBookingStatus(newStatus)
//                            bookingViewModel.updateBookingToFirebaase()
//                            /* update selected booking status, update to firebase */
//                        },
//                        bookingViewModel = bookingViewModel
//                    )
                        AppointmentList(
                            bookingList = filteredList,
                            onStatusChange = { booking, newStatus ->
                                bookingViewModel.getSelectedBooking(booking)
                                bookingViewModel.updateSelectedBookingStatus(newStatus)
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppointmentCard(
    booking: AdminBookingHistory,
    onStatusChange: (Booking, Int) -> Unit,
    bookingViewModel: BookingViewModel
) {
    val backgroundColor = bookingViewModel.getBookingStatusColor(booking.booking.status)
    val contentColor = bookingViewModel.getBookingStatusTextColor(booking.booking.status)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val cardSize = if (isTablet) 120.dp else 100.dp
    val cardFontSize = if (isTablet) 20.sp else 16.sp
    val otherFontSize = if (isTablet) 16.sp else 12.sp
    val cardImageSize = if (isTablet) 32.dp else 20.dp
    val bottonSize = if (isTablet) 76.dp else 72.dp

    Row(
        modifier = Modifier
            .height(cardSize)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.user),
                    contentDescription = null,
                    modifier = Modifier.size(cardImageSize),
                    colorFilter = ColorFilter.tint(contentColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = booking.booking.bookerId,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = contentColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = booking.phone,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = otherFontSize,
                    color = contentColor
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.booking_history_reason),
                    contentDescription = null,
                    modifier = Modifier.size(cardImageSize),
                    colorFilter = ColorFilter.tint(contentColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = booking.booking.bookingReason,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = contentColor
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = bookingViewModel.formatTimestampToTime(booking.booking.bookDate),
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = cardFontSize,
                    color = contentColor
                )
            }
        }

        if (booking.booking.status == 0) {
            Column(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .width(bottonSize)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF469A8A)),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    onClick = {
                        onStatusChange(booking.booking, 1)
                    }
                ) {
                    Text(
                        stringResource(R.string.accept_button),
                        fontSize = otherFontSize,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    modifier = Modifier
                        .width(bottonSize)
                        .weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC453A)),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    onClick = {
                        onStatusChange(booking.booking, 2)
                    }
                ) {
                    Text(
                        stringResource(R.string.reject_button),
                        fontSize = otherFontSize,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun AppointmentList(
    bookingList: List<AdminBookingHistory>,
    onStatusChange: (Booking, Int) -> Unit,
    bookingViewModel: BookingViewModel
) {
    val groupedBookings = bookingList.groupBy { booking ->
        bookingViewModel.getFormattedDateFromTimestamp(booking.booking.bookDate)
    }
        .toSortedMap(compareByDescending { it })

    LazyColumn {
        groupedBookings.forEach { (date, bookings) ->
            item {
                val dayOfWeek =
                    bookingViewModel.getDayOfWeekFromTimestamp(bookings.first().booking.bookDate)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = dayOfWeek,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                if (bookings.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_appointment),
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            items(bookings.size) { index ->
                val booking = bookings[index]
                AppointmentCard(
                    booking = booking,
                    onStatusChange = onStatusChange,
                    bookingViewModel = bookingViewModel
                )
            }
        }
    }
}

@Composable
fun AppointmentContent(
    bookings: List<AdminBookingHistory>,
    twoMonthsDates: List<LocalDate>,
    onStatusChange: (Booking, Int) -> Unit,
    bookingViewModel: BookingViewModel
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(Color.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        BookingListByDate(
            allDates = twoMonthsDates,
            bookings = bookings,
            onStatusChange = onStatusChange,
            bookingViewModel = bookingViewModel
        )
        AppointmentList(
            bookingList = bookings,
            onStatusChange = onStatusChange,
            bookingViewModel = bookingViewModel
        )
    }
}


@SuppressLint("NewApi")
@Composable
fun BookingListByDate(
    allDates: List<LocalDate>,
    bookings: List<AdminBookingHistory>,
    onStatusChange: (Booking, Int) -> Unit,
    bookingViewModel: BookingViewModel
) {
    val inputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)

    val grouped = bookings.groupBy {
        val date = LocalDate.parse(
            bookingViewModel.getFormattedDateFromTimestamp(it.booking.bookDate),
            inputFormatter
        )
        date.format(outputFormatter)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val dailyBookingsSize = if (isTablet) 80.dp else 60.dp
    val bookingListFontSize = if (isTablet) 20.sp else 16.sp

    LazyColumn {
        items(allDates.size) { index ->
            val date = allDates[index]
            val dailyBookings = grouped[date.format(outputFormatter)] ?: emptyList()
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                val dayOfWeek = date.dayOfWeek.toString().take(3)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        date.format(formatter),
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = bookingListFontSize
                    )
                    Text(
                        dayOfWeek,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = bookingListFontSize
                    )
                }
                if (dailyBookings.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(dailyBookingsSize),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = stringResource(R.string.no_appointment),
//                            fontFamily = poppinsFontFamily,
//                            fontWeight = FontWeight.Medium,
//                            fontSize = bookingListFontSize,
//                            color = Color.Gray
//                        )
//                    }
                } else {
                    dailyBookings.forEach { booking ->
                        AppointmentCard(
                            booking = booking,
                            onStatusChange = onStatusChange,
                            bookingViewModel = bookingViewModel
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
fun getTwoMonthsDateRange(): List<LocalDate> {
    val today = LocalDate.now()
    val startDate = today.minusMonths(1)
    val endDate = today.plusMonths(1)

    val dates = mutableListOf<LocalDate>()
    var current = startDate

    while (!current.isAfter(endDate)) {
        dates.add(current)
        current = current.plusDays(1)
    }

    return dates
}