package com.example.ass1.ui.screen.community

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.Event
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.theme.poppinsFontFamily

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminCommunityListScreen(
    onNavigateToPostScreen: () -> Unit,
    onEditDetails: () -> Unit,
    onAddNewEvent: () -> Unit,
    communityViewModel: CommunityViewModel,
) {
    val event = communityViewModel.eventsList.value

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
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
                        .verticalScroll(rememberScrollState())
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {

                    //Event Card
                    event.forEach { event ->
                        //Event posted date
                        PostedDateCard(
                            event = event,
                            communityViewModel = communityViewModel
                        )

                        EditEventCard(
                            event = event,
                            onClick = {
                                communityViewModel.updateSelectedEvent(event.eventId)
                                onNavigateToPostScreen()
                            },
                            onEditDetails = {
                                communityViewModel.updateSelectedEvent(event.eventId)
                                communityViewModel.updateCurrentEvent(communityViewModel.eventsList.value.find { it.eventId == event.eventId}!!)
                                onEditDetails()
                            }
                        )
                    }
                }
            }

            ButtonAtBottom(
                onClick = { onAddNewEvent() },
                text = "Add New Event"
            )

        }
    }
}

@Composable
fun ButtonAtBottom(
    onClick: () -> Unit,
    text: String
) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 12.dp)
    ) {
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xff0e2e6b)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = text,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun EditEventCard(
    event: Event,
    onClick: () -> Unit,
    onEditDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffc4e3ff)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Event Picture
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .padding(8.dp)
            ) {
                val decodedBitmap = base64ToBitmap(event.imageUrl)
                decodedBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Event Picture",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //Event Title
            Text(
                text = event.eventTitle,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            )

            //Event Details
            Text(
                text = event.eventDetails,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontFamily = poppinsFontFamily
                ),
                textAlign = TextAlign.Justify,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            //'Edit Details' button
            Button(
                onClick = onEditDetails,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0e2e6b)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Edit Details",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFontFamily
                    ),
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}