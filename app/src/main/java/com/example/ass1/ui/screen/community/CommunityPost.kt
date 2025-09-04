package com.example.ass1.ui.screen.community

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.theme.poppinsFontFamily

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CommunityPostScreen(
    communityViewModel: CommunityViewModel
) {

    val communityUiState by communityViewModel.uiState.collectAsStateWithLifecycle()
    val event = communityViewModel.eventsList.value.find { it.eventId == communityUiState.selectedEvent }

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EventPicture(event!!.imageUrl)

                Spacer(modifier = Modifier.height(20.dp))

                EventTitle(event.eventTitle)

                Spacer(modifier = Modifier.height(16.dp))

                EventDetails(event.eventDetails)
            }
        }
    }
}

@Composable
fun EventPicture(
    imageUrl: String
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .height(180.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val decodedBitmap = base64ToBitmap(imageUrl)
        decodedBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Event Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun EventTitle(
    title: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun EventDetails(
    details: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = details,
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontFamily = poppinsFontFamily
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Justify
        )
    }
}