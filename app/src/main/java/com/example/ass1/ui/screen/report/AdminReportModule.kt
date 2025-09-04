package com.example.ass1.ui.screen.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.Report
import com.example.ass1.ui.component.AdminBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.screen.booking.BookingViewModel
import com.example.ass1.ui.theme.poppinsFontFamily

@Composable
fun AdminReportModule(
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
    reportViewModel: ReportViewModel
) {
    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    var selectedNavItem by remember { mutableStateOf(0) }

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    if (isTablet) {
        if (isLandscape){
            Row{
                SideBar(
                    currentScreen = "Report",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = { },
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
                            "Report",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        content()
                    }
                }
            }
        } else {
            Row{
                SideBar(
                    currentScreen = "Report",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = { },
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
                            "Report",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        content()
                    }
                }
            }
        }
    } else {
        if (isLandscape) {
            Row{
                SideBar(
                    currentScreen = "Report",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = { },
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
                            "Report",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        content()
                    }
                }
            }
        } else {
            Scaffold(
                topBar = {
                    MyTopBar(
                        "Report",
                        modifier = Modifier,
                        onNavigateBack = { onNavigateBack() },
                        isTablet = isTablet,
                        isLandscape = isLandscape,
                    )
                },
                bottomBar = {
                    AdminBottomBar(
                        selectedItem = selectedNavItem,
                        onClickHome = { onNavigateToHome() },
                        onClickCommunity = { onNavigateToCommunity() },
                    )
                }

            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun UpdateReportDialog(
    rpHistory: Report,
    onUpdate: () -> Unit,
    onDismiss: () -> Unit,
    reportViewModel: ReportViewModel
    //onAction: (Report) -> Unit = {}
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    var isStatusDropdownExpanded by remember { mutableStateOf(false) }
    //List of available status
    val statusOptions = listOf("Pending", "Processing", "Completed", "Canceled")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                //header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Report Details",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }
                if(rpHistory.reportImage.isNotBlank()) {
                    Divider(modifier = Modifier.padding(vertical = 12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    //Update scale with zoom factor
                                    scale = (scale * zoom).coerceIn(0.5f, 3f)

                                    //Update offset with pan
                                    offsetX += pan.x
                                    offsetY += pan.y
                                }
                            }
                    ) {
                        val decodedBitmap = base64ToBitmap(rpHistory.reportImage)
                        decodedBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Report image",
                                modifier = Modifier
                                    .size(200.dp)
                                    .graphicsLayer(
                                        scaleX = scale,
                                        scaleY = scale,
                                        translationX = offsetX,
                                        translationY = offsetY
                                    ),
                                contentScale = ContentScale.None
                            )
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                //Report Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Report ID:",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = rpHistory.reportCaseId,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Date:",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = reportViewModel.getFormattedDateFromTimestamp(rpHistory.reportTime),
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Text(
                        text = "Description:",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rpHistory.details,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        textAlign = TextAlign.Justify,
                        softWrap = true,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Divider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray
                )

                //Status
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Status:",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Box {
                        Row(
                            modifier = Modifier
                                .background(
                                    color = reportViewModel.getStatusColor(rpHistory.status),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { isStatusDropdownExpanded = true }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = reportViewModel.getStatus(rpHistory.status),
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily
                                )
                            )

                            Icon(
                                imageVector = Icons.Default.ExpandCircleDown,
                                contentDescription = "Expand",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        androidx.compose.material3.DropdownMenu(
                            expanded = isStatusDropdownExpanded,
                            onDismissRequest = { isStatusDropdownExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            statusOptions.forEach { status ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = status,
                                            style = TextStyle(
                                                fontFamily = poppinsFontFamily,
                                                fontSize = 16.sp
                                            )
                                        )
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(
                                                    color = reportViewModel.getStatusColor(
                                                        statusOptions.indexOf(status)
                                                    ),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                        )
                                    },
                                    onClick = {
                                        reportViewModel.updateClickReportStatus(statusOptions.indexOf(status))
                                        isStatusDropdownExpanded = false
                                    }
                                )
                            }
                        }

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Update Status button
                Button(
                    onClick = onUpdate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff0e2e6b)
                    )
                ) {
                    Text(
                        text = "Update Status",
                        style = TextStyle(
                            fontFamily = poppinsFontFamily,
                            color = Color.White
                        )
                    )
                }

            }
        }
    }
}