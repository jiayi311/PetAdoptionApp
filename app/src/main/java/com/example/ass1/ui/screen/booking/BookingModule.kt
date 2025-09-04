package com.example.ass1.ui.screen.booking

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.TabItem
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.component.TabBar
import com.example.ass1.ui.screen.donation.DonationTab
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookingModule(
    content: @Composable () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome : () -> Unit,
    onNavigateToCommunity : () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    onNavigateToBooking: () -> Unit,
    onNavigateToHistory: () -> Unit,
    bookingViewModel: BookingViewModel
) {

    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    val bookingUiState by bookingViewModel.uiState.collectAsStateWithLifecycle()
    var selectedNavItem by remember { mutableStateOf(0) }

    val tabs = listOf(
        TabItem(
            title = "Booking",
            route = DonationTab.Donation.name,
            command = {
                onNavigateToBooking()
                bookingViewModel.updateTabState(0)
            }
        ),
        TabItem(
            title = "History",
            route = DonationTab.History.name,
            command = {
                onNavigateToHistory()
                bookingViewModel.updateTabState(1)
            }
        )
    )

    if (isTablet) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            SideBar(
                currentScreen = stringResource(R.string.booking_history_title),
                onPetListClick = onNavigateToPetList,
                onCommunityClick = onNavigateToCommunity,
                onBookingListClick = {},
                onReportClick = onNavigateToReport,
                onDonationClick = onNavigateToDonation,
                onBackHome = onNavigateToHome,
                onProfileClick = onNavigateToProfile,
                onAboutUsClick = onNavigateToAboutUs,
                isTablet = isTablet,
                isLandscape = isLandscape
            )
            Scaffold(
                topBar = {
                    MyTopBar(
                        "Booking",
                        modifier = Modifier,
                        onNavigateBack = { onNavigateBack() },
                        isTablet = isTablet,
                        isLandscape = isLandscape
                    )
                }

            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TabBar(
                        tabs = tabs,
                        selectedTab = bookingUiState.tabState,
                        onTabSelected = { bookingViewModel.updateTabState(it) },
                        isTablet = isTablet,
                        isLandscape = isLandscape
                    )
                    content()
                }
            }
        }
    } else {
        Scaffold(
            topBar = {
                MyTopBar(
                    "Booking",
                    modifier = Modifier,
                    onNavigateBack = { onNavigateBack() },
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )
            },
            bottomBar = {
                MyBottomBar(
                    selectedItem = selectedNavItem,
                    onClickHome = { onNavigateToHome() },
                    onClickCommunity = { onNavigateToCommunity() },
                    onClickProfile = { onNavigateToProfile() }
                )
            }

        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabBar(
                    tabs = tabs,
                    selectedTab = bookingUiState.tabState,
                    onTabSelected = { bookingViewModel.updateTabState(it) },
                    isTablet,
                    isLandscape
                )
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFilterBar(
    modifier: Modifier = Modifier,
    reasonList: List<String>,
    onFilterChange: (selectedDate: Long?, selectedReason: String) -> Unit,
    bookingViewModel: BookingViewModel
) {
    var selectedText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var isExpanded by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val filterTextSize = if (isTablet) 16.sp else 12.sp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DatePickerFieldToModal(
            selectedDate = selectedDate,
            modifier = Modifier
                .padding(top = 16.dp)
                .width(172.dp),
            onDateSelected = {
                selectedDate = it
                onFilterChange(selectedDate, selectedText)
            },
            bookingViewModel = bookingViewModel
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.reset_filters),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .align(Alignment.End)
                    .clickable {
                        selectedDate = null
                        selectedText = ""
                        onFilterChange(null, "")
                    },
                color = Color.White,
                fontSize = filterTextSize,
                fontWeight = FontWeight.Medium
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
                onExpandedChange = { isExpanded = !isExpanded },
            ) {
                TextField(
                    modifier = Modifier
                        .height(52.dp)
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.filters_select_reason),
                            fontSize = filterTextSize,
                            color = Color.White
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = filterTextSize
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Icon",
                            tint = Color.White
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFF4A7ABF),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .background(Color(0xFFD5DFEB))
                ) {
                    reasonList.forEach { reason ->
                        DropdownMenuItem(
                            text = { Text(text = reason, color = Color.Black) },
                            onClick = {
                                selectedText = reason
                                isExpanded = false
                                onFilterChange(selectedDate, selectedText)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFilterBarLandscape(
    modifier: Modifier = Modifier,
    selectedText: String,
    onTextChange: (String) -> Unit,
    selectedDate: Long?,
    onDateChange: (Long?) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    reasonList: List<String>,
    bookingViewModel: BookingViewModel
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(220.dp)
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Row {
            Text(
                text = stringResource(R.string.filters),
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.reset_filters),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                        onDateChange(null)
                        onTextChange("")
                    },
                fontSize = 12.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        DatePickerFieldToModal(
            selectedDate = selectedDate,
            modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 4.dp,
                        topEnd = 4.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                ),
            onDateSelected = onDateChange,
            bookingViewModel = bookingViewModel
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = onExpandedChange
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .menuAnchor()
                    .clip(RoundedCornerShape(4.dp)),
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                placeholder = {
                    Text(
                        text = stringResource(R.string.filters_select_reason),
                        fontSize = 12.sp,
                        color = Color.White
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF4A7ABF),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                ),
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { onExpandedChange(false) },
                modifier = Modifier
                    .background(Color(0xFFD5DFEB))
            ) {
                reasonList.forEach { reason ->
                    DropdownMenuItem(
                        text = { Text(text = reason, color = Color.Black) },
                        onClick = {
                            onTextChange(reason)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerFieldToModal(
    selectedDate: Long?,
    modifier: Modifier = Modifier,
    onDateSelected: (Long) -> Unit,
    bookingViewModel: BookingViewModel
) {

    var showModal by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    val filterTextSize = if (isTablet) 16.sp else 12.sp

    TextField(
        value = selectedDate?.let { bookingViewModel.convertMillisToDate(it) } ?: "",
        onValueChange = { },
        readOnly = true,
        placeholder = {
            Text(
                stringResource(R.string.filters_select_date),
                color = Color.White,
                fontFamily = poppinsFontFamily,
                fontSize = filterTextSize
            )
        },
        textStyle = TextStyle(
            color = Color.White,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = filterTextSize
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Select date",
                tint = Color.White,
                modifier = Modifier
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            containerColor = Color(0xFF4A7ABF),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { date ->
                if (date != null) {
                    onDateSelected(date)
                }
                showModal = false
            },
            onDismiss = { showModal = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)
                .background(Color.DarkGray, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                DatePicker(state = datePickerState)
                Spacer(Modifier.height(16.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    TextButton(onClick = {
                        onDateSelected(datePickerState.selectedDateMillis)
                        onDismiss()
                    }) { Text("OK") }
                }
            }
        }
    }
}
