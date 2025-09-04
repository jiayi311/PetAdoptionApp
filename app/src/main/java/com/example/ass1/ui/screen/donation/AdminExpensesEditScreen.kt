package com.example.ass1.ui.screen.donation

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.component.bitmapToBase64
import com.example.ass1.ui.screen.community.ButtonAtBottom
import com.example.ass1.ui.screen.community.CommunityViewModel
import com.example.ass1.ui.screen.community.Title
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

@Composable
fun AdminExpensesEditScreen(
    onNavigateToExpensesList: () -> Unit,
    donationViewModel: DonationViewModel
) {

    val donateUiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        donateUiState.isEdit,
        donateUiState.isLoading,
        donateUiState.isEditSuccessful
    ) {
        // Only proceed if donate was made and loading has finished
        if (donateUiState.isEdit && !donateUiState.isLoading) {
            if (donateUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Successfully edit expenses!",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                donationViewModel.resetDonationForm()
                onNavigateToExpensesList()
            } else {
                snackbarHostState.showSnackbar(
                    message = "The expenses is fail to update. Please try again.",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .background(Color(0xFF4A7ABF))
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(Color.White)
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        DatePicker(donationViewModel)

                        Spacer(modifier = Modifier.height(8.dp))

                        TimePicker(donationViewModel)

                        Spacer(modifier = Modifier.height(8.dp))

                        PaymentMethodDropdown(donationViewModel)

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        CategoryDropdown(donationViewModel)

                        Spacer(modifier = Modifier.height(8.dp))

                        ItemsField(donationViewModel)

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )

                        Text(
                            text = "Document Upload:",
                            style = TextStyle(
                                color = Color(0xff0e2e6b),
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsFontFamily,
                                fontSize = 16.sp
                            )
                        )

                        //Upload document function
                        ExpensesPicEditable(
                            img = donateUiState.newExpenses.imageUrl,
                            donationViewModel = donationViewModel
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = 0.5.dp,
                            color = Color.LightGray
                        )

                        TotalAmount(donationViewModel)

                    }
                }

                ButtonAtBottom(
                    onClick = {
                        if (donationViewModel.validateAddExpensesForm()) {
                            donationViewModel.submitEditExpenses()

                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = donateUiState.errorMessage!!,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    },
                    text = "Save Changes"
                )
            }
        }
    }
}

@Composable
fun ExpensesPicEditable(
    img: String,
    donationViewModel: DonationViewModel
) {
    val context = LocalContext.current
    val uiState by donationViewModel.uiState.collectAsStateWithLifecycle()

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

                    donationViewModel.uploadImages(base64String)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .height(180.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable{imagePickerLauncher.launch("image/*")},
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
                    val decodedBitmap = base64ToBitmap(uiState.newExpenses.imageUrl)
                    decodedBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Expenses Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
