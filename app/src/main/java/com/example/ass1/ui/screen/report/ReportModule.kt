package com.example.ass1.ui.screen.report

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.Report
import com.example.ass1.model.TabItem
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.component.SubmitReportDialog
import com.example.ass1.ui.component.TabBar
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.component.bitmapToBase64
import com.example.ass1.ui.theme.comicSansFontFamily
import com.example.ass1.ui.theme.poppinsFontFamily
import java.io.InputStream

enum class ReportTab(@StringRes val title: Int) {
    Report(title = R.string.Report),
    History(title = R.string.History)
}

@Composable
fun ReportModule(
    onNavigateBack: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToHome : () -> Unit,
    onNavigateToCommunity : () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    onNavigateToAboutUs: () -> Unit,
    reportViewModel: ReportViewModel,
    content: @Composable () -> Unit
) {

    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    val submitResult by reportViewModel.submitResult.collectAsStateWithLifecycle()
    var showSuccessfullySubmitDialog by rememberSaveable { mutableStateOf(false) }
    var showFalseSubmitDialog by rememberSaveable { mutableStateOf(false) }
    var selectedNavItem by remember { mutableStateOf(0) }
    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    // the selected by default is report screen
    //will change when user click on other screen

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    // will trigger whenever the submitResult value change and not null
    LaunchedEffect(submitResult) {
        submitResult?.let { success ->
            if (success) {
                showSuccessfullySubmitDialog = true
            }else{
                showFalseSubmitDialog = true
            }
            reportViewModel.resetSubmitResult()
        }
    }

    val tabs = listOf(
        TabItem(
            title = "Report",
            route = ReportTab.Report.name,
            command = {
                onNavigateToReport()
                reportViewModel.updateTabState(0)
            }
        ),
        TabItem(
            title = "History",
            route = ReportTab.History.name,
            command = {
                onNavigateToHistory()
                reportViewModel.updateTabState(1 )
            }
        )
    )

    if(isTablet) {
        if(isLandscape) {
            Row{
                SideBar(
                    currentScreen = "Report",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = {},
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
                        TabBar(
                            tabs = tabs,
                            selectedTab = reportUiState.tabState,
                            onTabSelected = { reportViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )

                        content()
                    }

                    if (showSuccessfullySubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.summit_successfully),
                            text = stringResource(R.string.successfully_submit_report_dialog),
                            icon = Icons.Default.Task,
                            confirmButtonText = stringResource(R.string.report_more)
                        )
                    }

                    if (showFalseSubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.report_failed),
                            text = stringResource(R.string.failed_submit_report_dialog),
                            icon = Icons.Default.Error,
                            confirmButtonText = stringResource(R.string.report_again)
                        )
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
                    onReportClick = {},
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
                        TabBar(
                            tabs = tabs,
                            selectedTab = reportUiState.tabState,
                            onTabSelected = { reportViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )

                        content()
                    }

                    if (showSuccessfullySubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.summit_successfully),
                            text = stringResource(R.string.successfully_submit_report_dialog),
                            icon = Icons.Default.Task,
                            confirmButtonText = stringResource(R.string.report_more)
                        )
                    }

                    if (showFalseSubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.report_failed),
                            text = stringResource(R.string.failed_submit_report_dialog),
                            icon = Icons.Default.Error,
                            confirmButtonText = stringResource(R.string.report_again)
                        )
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
                    onReportClick = {},
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
                        TabBar(
                            tabs = tabs,
                            selectedTab = reportUiState.tabState,
                            onTabSelected = { reportViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )

                        content()
                    }

                    if (showSuccessfullySubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showSuccessfullySubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.summit_successfully),
                            text = stringResource(R.string.successfully_submit_report_dialog),
                            icon = Icons.Default.Task,
                            confirmButtonText = stringResource(R.string.report_more)
                        )
                    }

                    if (showFalseSubmitDialog) {
                        SubmitReportDialog(
                            onReportAgain = {
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            onClickCloseDialog = {
                                onNavigateToHistory()
                                reportViewModel.updateTabState(1)
                                showFalseSubmitDialog = false
                                reportViewModel.resetReportForm()
                            },
                            title = stringResource(R.string.report_failed),
                            text = stringResource(R.string.failed_submit_report_dialog),
                            icon = Icons.Default.Error,
                            confirmButtonText = stringResource(R.string.report_again)
                        )
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
                    MyBottomBar(
                        selectedItem = selectedNavItem,
                        onClickHome = { onNavigateToHome() },
                        onClickCommunity = { onNavigateToCommunity() },
                        onClickProfile = { onNavigateToProfile() },
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
                        selectedTab = reportUiState.tabState,
                        onTabSelected = { reportViewModel.updateTabState(it) },
                        isTablet = isTablet,
                        isLandscape = isLandscape,
                    )

                    content()
                }

                if (showSuccessfullySubmitDialog) {
                    SubmitReportDialog(
                        onReportAgain = {
                            showSuccessfullySubmitDialog = false
                            reportViewModel.resetReportForm()
                        },
                        onClickCloseDialog = {
                            onNavigateToHistory()
                            reportViewModel.updateTabState(1)
                            showSuccessfullySubmitDialog = false
                            reportViewModel.resetReportForm()
                        },
                        title = stringResource(R.string.summit_successfully),
                        text = stringResource(R.string.successfully_submit_report_dialog),
                        icon = Icons.Default.Task,
                        confirmButtonText = stringResource(R.string.report_more)
                    )
                }

                if (showFalseSubmitDialog) {
                    SubmitReportDialog(
                        onReportAgain = {
                            showFalseSubmitDialog = false
                            reportViewModel.resetReportForm()
                        },
                        onClickCloseDialog = {
                            onNavigateToHistory()
                            reportViewModel.updateTabState(1)
                            showFalseSubmitDialog = false
                            reportViewModel.resetReportForm()
                        },
                        title = stringResource(R.string.report_failed),
                        text = stringResource(R.string.failed_submit_report_dialog),
                        icon = Icons.Default.Error,
                        confirmButtonText = stringResource(R.string.report_again)
                    )
                }
            }
        }
    }
}

@Composable
fun ReportScreenTextField(
    value: String,
    fieldName: String,
    helpText: String,
    onValueChange: (String) -> Unit,
    modifier:Modifier = Modifier
) {

    Column() {
        Text(
            fieldName,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.align(Alignment.Start)
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    helpText,
                    fontFamily = comicSansFontFamily,
                    color = Color(0xFF8c8787)
                )
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),

            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),

            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 112.dp)
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
fun ReportCaseButton(
    onClick: () -> Unit,
    location: String,
    details: String,
    isLoading: Boolean
) {
    val isEnabled = location.isNotBlank() && details.isNotBlank() && !isLoading
    Button(
        onClick = { onClick() },
        enabled = isEnabled,
        shape = RoundedCornerShape(8.0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xff0e2e6b) ,
            contentColor =  Color.White,
            disabledContainerColor = Color(0xff838383),
            disabledContentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Text(
            "REPORT NOW",
            fontFamily = comicSansFontFamily,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ReportImageAttachmentSection(
    uploadedImage: UploadedImage?,
    onImageUploaded: (UploadedImage) -> Unit,
    onImageRemoved: (UploadedImage?) -> Unit,
    reportViewModel: ReportViewModel
) {
    val context = LocalContext.current
    val uiState by reportViewModel.uiState.collectAsStateWithLifecycle()

    // Image picker launcher for single image
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                // Get input stream from URI
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                inputStream?.use { stream ->
                    // Get file name from URI
                    val fileName = uri.lastPathSegment ?: "image_${System.currentTimeMillis()}"

                    // Convert to bitmap
                    val bitmap = BitmapFactory.decodeStream(stream)

                    // Convert bitmap to base64
                    val base64String = bitmapToBase64(bitmap)

                    val uploadedImage = UploadedImage(
                        name = fileName,
                        base64Data = base64String
                    )

                    // Update with the new single image (replacing any previous image)
                    onImageUploaded(uploadedImage)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Attachment:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { imagePickerLauncher.launch("image/*") },
            border = BorderStroke(
                width = 1.dp,
                color = Color.Gray.copy(alpha = 0.5f)
            ),
            shape = MaterialTheme.shapes.medium,
            color = Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (uiState.uploadedImageName == "") {
                    // Show upload UI when no images
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Upload Image",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Upload Image",
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Show uploaded image
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.uploadedImageName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            // Add remove button
                            IconButton(
                                onClick = { onImageRemoved(uploadedImage) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove image",
                                    tint = Color.Red.copy(alpha = 0.7f)
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
fun ReportHistoryCard(
    history: Report,
    onClick: () -> Unit,
    reportViewModel: ReportViewModel
) {

    val cardColor = reportViewModel.getStatusColor(history.status)

    Card(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {

            //Report icon & id
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                //Record Icon
                Image(
                    painter = painterResource(R.drawable.record_icon),
                    contentDescription = "Record Icon",
                    modifier = Modifier
                        .size(28.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                //report id
                Text(
                    text = history.reportCaseId,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        fontFamily = poppinsFontFamily
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Date of reporting
                Text(
                    text = reportViewModel.getFormattedDateFromTimestamp(history.reportTime),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = poppinsFontFamily
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = reportViewModel.getStatus(history.status),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = poppinsFontFamily
                            )
                        )
                    }
                }

            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ReportDialog(
    rpHistory: Report,
    onDismiss: () -> Unit,
    onDelete:() -> Unit,
    reportViewModel: ReportViewModel
    //onAction: (Report) -> Unit = {}
) {
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

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
                    //Report Image
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status:",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = reportViewModel.getStatus(rpHistory.status),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    //Delete Report when the status is 'Pending'
                    Button(
                        onClick = onDelete,
                        enabled = rpHistory.status == 0,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text(
                            text = "Delete Report",
                            style = TextStyle(
                                fontFamily = poppinsFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Close button
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff0e2e6b)
                        )
                    ) {
                        Text(
                            text = "Close Report Details",
                            style = TextStyle(
                                fontFamily = poppinsFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        )
                    }

                }

            }
        }
    }
}
