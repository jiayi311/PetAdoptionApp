package com.example.ass1.ui.screen.donation

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.ExpenseItem
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.theme.poppinsFontFamily
import com.google.firebase.Timestamp


@Composable
fun ExpensesScreen(
    donationViewModel: DonationViewModel
) {
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
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp, horizontal = 28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val groupedExpenses = expenses.groupBy { expense ->
                    donationViewModel.getFormattedDateFromTimestamp(expense.receipt.timestamp)
                }.toSortedMap(compareByDescending {it}) // Sort by date descending

                groupedExpenses.forEach { (date, expensesForDate) ->

                    val dayOfWeek = donationViewModel.getDayOfWeekFromTimestamp(expensesForDate.first().receipt.timestamp)
                    DateRow(day = dayOfWeek, date = date)

                    //Expenses Amount Card
                    expensesForDate.forEach { expense ->
                        ExpensesCard(
                            expense = expense,
                            onClick = { selectedExpense = expense }
                        )
                    }
                }


                //Show dialog when expenses is selected
                if (selectedExpense != null) {
                    ReceiptDialog(
                        expense = selectedExpense!!,
                        onDismiss = { selectedExpense = null },
                        donationViewModel = donationViewModel
                    )
                }

            }
        }
    }
}

@Composable
fun DateRow(day: String, date: String) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        //Date
        Text(
            text = date,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily
            )
        )
        //
        Text(
            text = day,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily
            )
        )
    }
}

@Composable
fun ExpensesCard(
    expense: ExpenseItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffd5dfeb)),
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
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun ReceiptDialog(
    expense: ExpenseItem,
    onDismiss: () -> Unit,
    donationViewModel: DonationViewModel
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
                        text = "Receipt Details",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                if(expense.imageUrl.isNotBlank()) {
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
                        //Receipt Image
                        val decodedBitmap = base64ToBitmap(expense.imageUrl)
                        decodedBitmap?.let { bitmap ->
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Expense image",
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

                //Receipt Details
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
                        text = donationViewModel.formatTimestamp(expense.receipt.timestamp),
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
                        text = "Time:",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = donationViewModel.formatTimestampToTime(expense.receipt.timestamp),
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
                        text = "Payment:",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = expense.receipt.paymentMethod,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // Items
                Text(
                    text = "Items:",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFontFamily
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                expense.receipt.items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.name,
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            )
                        )
                        Text(
                            text = String.format("%.2f", item.price),
                            style = TextStyle(
                                fontFamily = poppinsFontFamily
                            )
                        )
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        thickness = 0.5.dp,
                        color = Color.LightGray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Total
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total:",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = String.format("%.2f", expense.amount),
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFontFamily
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                        text = "Close Receipt",
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