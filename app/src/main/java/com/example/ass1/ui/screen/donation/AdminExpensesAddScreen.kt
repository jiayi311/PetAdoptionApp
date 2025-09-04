package com.example.ass1.ui.screen.donation

import android.app.TimePickerDialog
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.component.bitmapToBase64
import com.example.ass1.ui.screen.community.ButtonAtBottom
import com.example.ass1.ui.screen.report.ReportViewModel
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AdminExpensesAddScreen(
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
                    message = "Successfully add expenses!",
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
                donationViewModel.resetDonationForm()
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
                        //ExpenseId(donationViewModel)

                        //Spacer(modifier = Modifier.height(8.dp))

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
                        ImageAttachmentSection(
                            uploadedImage = UploadedImage(
                                name = donateUiState.imageName,
                                base64Data = donateUiState.newExpenses.imageUrl
                            ),
                            onImageUploaded = { newImage ->
                                donationViewModel.addImages(newImage)
                            },
                            onImageRemoved = { donationViewModel.removeImage() },
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
                            donationViewModel.submitNewExpenses()

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
                    text = "Add Expense"
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    donationViewModel: DonationViewModel
) {
    val calendar = remember { Calendar.getInstance() }
    var selectedDate by remember { mutableStateOf(calendar.timeInMillis) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Date:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F1FF))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { showDatePicker = true }
        ) {
            Text(
                text = dateFormatter.format(Date(selectedDate)),
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily
                )
            )
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDate = millis
                        donationViewModel.updateSelectedDate(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun TimePicker(
    donationViewModel: DonationViewModel
) {
    val calendar = remember { Calendar.getInstance() }
    var selectedTime by remember { mutableStateOf(calendar.timeInMillis) }
    var showTimePicker by remember { mutableStateOf(false) }

    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val context = LocalContext.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Time:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F1FF))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { showTimePicker = true }
        ) {
            Text(
                text = timeFormatter.format(Date(selectedTime)),
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily
                )
            )
        }
    }

    if (showTimePicker) {
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                selectedTime = calendar.timeInMillis
                donationViewModel.updateSelectedTime(calendar.timeInMillis)
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // 12-hour format
        )

        // Show the time picker dialog
        timePickerDialog.show()

        // Reset the flag after showing the dialog
        showTimePicker = false
    }
}

@Composable
fun PaymentMethodDropdown(
    donationViewModel: DonationViewModel
) {
    val uiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    val paymentMethods = listOf("E-wallet", "Cash", "Card", "Online Banking")
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Payment Method:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F1FF))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(
                    text = uiState.newExpenses.receipt.paymentMethod,
                    style = TextStyle(
                        color = Color(0xff0e2e6b),
                        fontSize = 16.sp,
                        fontFamily = poppinsFontFamily
                    )
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color(0xff0e2e6b)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                paymentMethods.forEach { method ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = method,
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily
                                )
                            )
                        },
                        onClick = {
                            donationViewModel.updateExpPaymentMethod(method)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryDropdown(
    donationViewModel: DonationViewModel
) {
    val uiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    val categories = listOf(
        "Food",
        "Medical",
        "Vaccine",
        "Hygiene",
        "Housing"
    )
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Category:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE8F1FF))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    text = uiState.newExpenses.category,
                    style = TextStyle(
                        color = Color(0xff0e2e6b),
                        fontSize = 16.sp,
                        fontFamily = poppinsFontFamily
                    )
                )

                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = Color(0xff0e2e6b)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                style = TextStyle(
                                    color = Color(0xff0e2e6b),
                                    fontSize = 16.sp,
                                    fontFamily = poppinsFontFamily
                                )
                            )
                        },
                        onClick = {
                            donationViewModel.updateExpCategory(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ItemsField(
    donationViewModel: DonationViewModel
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Items:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Item Name",
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFontFamily
                ),
                modifier = Modifier.weight(0.7f)
            )

            Text(
                text = "Amount (RM)",
                style = TextStyle(
                    color = Color(0xff0e2e6b),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFontFamily
                ),
                modifier = Modifier.weight(0.3f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 5 rows of item name and amount fields
        repeat(5) { index ->
            ItemRow(
                index,
                donationViewModel = donationViewModel
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemRow(
    index: Int,
    donationViewModel: DonationViewModel
) {

    val uiState by donationViewModel.uiState.collectAsStateWithLifecycle()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Item name field
        OutlinedTextField(
            value = uiState.newReceiptItemList[index].name,
            onValueChange = { donationViewModel.updateReceiptItemListName(
                it,
                index = index
            ) },
            modifier = Modifier
                .weight(0.7f)
                .padding(end = 8.dp),
            placeholder = { Text("Item ${index + 1}") },
            singleLine = true,
            textStyle = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A7ABF),
                unfocusedBorderColor = Color(0xFFE8F1FF),
                focusedContainerColor = Color(0xFFF5F9FF),
                unfocusedContainerColor = Color(0xFFE8F1FF)
            )
        )

        var itemAmount by remember { mutableStateOf(uiState.newReceiptItemList[index].price.toString()) }

        // Item amount field
        OutlinedTextField(
            value = itemAmount,  // Use the local state variable for display
            onValueChange = { newValue ->
                // Only allow numeric input with decimal point
                if (newValue.isEmpty() || newValue.matches(Regex("\\d*\\.?\\d*"))) {
                    // Update the local variable for UI display
                    itemAmount = newValue

                    // Update the data model with converted value
                    try {
                        val priceValue = if (newValue.isEmpty()) 0.0 else newValue.toDouble()
                        donationViewModel.updateReceiptItemListPrice(priceValue, index)
                    } catch (e: NumberFormatException) {
                        Log.e("ReceiptItem", "Invalid number format: $newValue")
                    }
                }
            },
            modifier = Modifier.weight(0.3f),
            placeholder = { Text("0.00") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            textStyle = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 14.sp,
                fontFamily = poppinsFontFamily
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4A7ABF),
                unfocusedBorderColor = Color(0xFFE8F1FF),
                focusedContainerColor = Color(0xFFF5F9FF),
                unfocusedContainerColor = Color(0xFFE8F1FF)
            )
        )
    }
}

@Composable
fun TotalAmount(
    donationViewModel: DonationViewModel
) {
    val uiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    donationViewModel.calculateTotal()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total:",
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFontFamily
            )
        )
        Text(
            text = String.format("%.2f", uiState.newExpenses.amount),
            style = TextStyle(
                color = Color(0xff0e2e6b),
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
fun ImageAttachmentSection(
    uploadedImage: UploadedImage?,
    onImageUploaded: (UploadedImage) -> Unit,
    onImageRemoved: (UploadedImage?) -> Unit,
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
                if (uiState.imageName == "") {
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
                                text = uiState.imageName,
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
