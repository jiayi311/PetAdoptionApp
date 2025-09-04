package com.example.ass1.ui.screen.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.theme.comicSansFontFamily

@Composable
fun ReportScreen(
    reportViewModel: ReportViewModel,
    onReportNowClick: () -> Unit,
) {

    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by reportViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by reportViewModel.errorMessage.collectAsStateWithLifecycle()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    errorMessage?.let { message ->
        Text(
            text = message,
            color = Color.Red
        )
    }

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.White)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Image(
                    painter = painterResource(R.drawable.report_sick_dog),
                    contentDescription = "injured dog",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .width(220.dp)
                        .height(160.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(
                            start = 32.dp,
                            end = 32.dp,
                            bottom = 56.dp
                        )
                        .clip(shape = RoundedCornerShape(8.dp))
                        .fillMaxSize()
                        .background(color = Color(0xFFe7f0f7))
                        .padding(8.dp)
                ) {

                    Text(
                        "Report Injured Animal",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = comicSansFontFamily,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier
                            .size(32.dp)
                    )

                    Text(
                        "Help us locate and assist injured animals by providing the details below.",
                        fontWeight = FontWeight.Normal,
                        fontFamily = comicSansFontFamily,
                        fontStyle = FontStyle.Italic,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.7.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .size(32.dp)
                    )

                    ReportScreenTextField(
                        fieldName = "Location",
                        helpText = "Enter Location",
                        onValueChange = { reportViewModel.updateLocation(it) },
                        value = reportUiState.reportLocation
                    )

                    Spacer(
                        modifier = Modifier
                            .size(20.dp)
                    )

                    ReportScreenTextField(
                        fieldName = "Details",
                        helpText = "Enter Details of the Injured Animals",
                        onValueChange = { reportViewModel.updateDetails(it) },
                        value = reportUiState.reportDetails
                    )

                    ReportImageAttachmentSection(
                        uploadedImage = UploadedImage(
                            name = reportUiState.uploadedImageName,
                            base64Data = reportUiState.uploadedImageString
                        ),
                        onImageUploaded = { newImage ->
                            reportViewModel.addImages(newImage)
                        },
                        onImageRemoved = { reportViewModel.removeImage() },
                        reportViewModel = reportViewModel
                    )

                    Spacer(
                        modifier = Modifier
                            .size(28.dp)
                    )

                    ReportCaseButton (
                        onClick = onReportNowClick,
                        location = reportUiState.reportLocation,
                        details = reportUiState.reportDetails,
                        isLoading = isLoading
                    )

                    Spacer(
                        modifier = Modifier
                            .size(4.dp)
                    )
                }
            }
        }
    }
}

