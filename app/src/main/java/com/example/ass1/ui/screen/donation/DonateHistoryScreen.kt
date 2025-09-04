package com.example.ass1.ui.screen.donation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.Donation
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.forEach

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DonationHistoryScreen(
    donationViewModel: DonationViewModel
) {

    val historyRecord = donationViewModel.userDonationList.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp, horizontal = 28.dp),
            ) {
                items(historyRecord.value) { donation ->
                    DonationHistoryCard(
                        history = donation,
                        donationViewModel = donationViewModel
                    )
                }

                // If the list is empty, show a message
                if (historyRecord.value.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No donation history found",
                                fontFamily = poppinsFontFamily,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun DonationHistoryCard(
    history: Donation,
    donationViewModel: DonationViewModel
) {
    Card(
        modifier = Modifier
            .height(88.dp)
            .fillMaxWidth(),
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

            //Report icon & donation id
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

                //donation id
                Text(
                    text = history.donationId,
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
                //Date of donation
                Text(
                    text = donationViewModel.formatTimestamp(history.donateTime),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = poppinsFontFamily
                    )
                )


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

                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier.width(60.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        //Amount of donation
                        Text(
                            text = String.format("%.2f", history.donateAmount),
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = poppinsFontFamily
                            )
                        )
                    }
                }

            }


            //Amount
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))

}