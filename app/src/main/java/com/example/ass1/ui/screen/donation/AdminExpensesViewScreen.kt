package com.example.ass1.ui.screen.donation

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.ExpenseItem
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.component.bitmapToBase64
import com.example.ass1.ui.screen.community.ButtonAtBottom
import com.example.ass1.ui.theme.poppinsFontFamily
import java.io.InputStream

@Composable
fun AdminExpensesViewScreen(
    onNavigateToEdit: () -> Unit,
    onAddNew: () -> Unit,
    donationViewModel: DonationViewModel
) {
    val donationUiState by donationViewModel.uiState.collectAsStateWithLifecycle()
    donationViewModel.fetchExpenseItemList()

    val expenses by donationViewModel.expensesItemList.collectAsStateWithLifecycle()
    var selectedExpense by remember { mutableStateOf<ExpenseItem?>(null) }

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(top = 20.dp, start = 28.dp, end = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    val groupedExpenses = expenses.groupBy { expense ->
                        donationViewModel.getFormattedDateFromTimestamp(expense.receipt.timestamp)
                    }.toSortedMap(compareByDescending {it}) // Sort by date descending

                    groupedExpenses.forEach { (date, expensesForDate) ->

                        val dayOfWeek = donationViewModel.getDayOfWeekFromTimestamp(expensesForDate.first().receipt.timestamp)
                        DateRow(day = dayOfWeek, date = date)

                        //Expenses Amount Card
                        expensesForDate.forEach { expense ->
                            EditExpensesCard(
                                expense = expense,
                                onClick = { selectedExpense = expense },
                                onEditExpense = {
                                    donationViewModel.updateSelectedExpenses(expense)
                                    onNavigateToEdit()
                                },
                                onUpload = {
                                    donationViewModel.updateSelectedExpenses(expense)
                                },
                                donationViewModel = donationViewModel
                            )
                        }
                    }
                }
            }

            ButtonAtBottom(
                onClick = { onAddNew() },
                text = "Add New Expense"
            )
        }
    }
}

@Composable
fun EditExpensesCard(
    expense: ExpenseItem,
    onClick: () -> Unit,
    onEditExpense: () -> Unit,
    onUpload: () -> Unit,
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
                    donationViewModel.updateSelectedImage(base64String)
                    donationViewModel.updateEditImg()

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Card(
        modifier = Modifier
            .height(152.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffd5dfeb)
        ),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {

            //Report icon & expense id
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

                //expense id
                Text(
                    text = expense.id,
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                //Black Paw Icon & Details for expenses
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Black Paw Icon
                    Image(
                        painter = painterResource(R.drawable.black_paw),
                        contentDescription = "Black Paw Icon",
                        modifier = Modifier
                            .size(28.dp)

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    //Details for expenses
                    Text(
                        text = expense.category,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                //Dollar Sign & Amount
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Dollar Sign Icon
                    Image(
                        painter = painterResource(R.drawable.dollar_sign_icon),
                        contentDescription = "Dollar Sign Icon",
                        modifier = Modifier
                            .size(28.dp)

                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    //Amount
                    Text(
                        text = String.format("%.2f", expense.amount),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //'Edit Expense' & 'Upload Document' buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                //'Edit Expense' button
                Button(
                    onClick = { onEditExpense() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff0e2e6b)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Edit Expense",
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = {
                        onUpload()
                        imagePickerLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff0e2e6b)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Upload Document",
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = poppinsFontFamily,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}