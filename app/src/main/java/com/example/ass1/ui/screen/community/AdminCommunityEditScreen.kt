package com.example.ass1.ui.screen.community

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.component.bitmapToBase64
import com.example.ass1.ui.screen.aboutUs.EditableField
import com.example.ass1.ui.theme.poppinsFontFamily
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminCommunityEditScreen(
    onNavigateToCommunity: () -> Unit,
    communityViewModel: CommunityViewModel
) {

    val communityUiState by communityViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (communityUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        communityUiState.isEdit,
        communityUiState.isLoading,
        communityUiState.isEditSuccessful
    ) {
        // Only proceed if register was made and loading has finished
        if (communityUiState.isEdit && !communityUiState.isLoading) {
            if (communityUiState.isEditSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Update data successful",
                        duration = SnackbarDuration.Short
                    )

                    delay(200)
                    onNavigateToCommunity()
                    communityViewModel.resetEditForm()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to update data. Please try again.",
                        duration = SnackbarDuration.Short
                    )
                    communityViewModel.resetEditForm()
                }
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,
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
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        EventPictureEditable(
                            eventImg = communityUiState.imageUrl,
                            communityViewModel = communityViewModel
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Title(
                            title = "Title:",
                            color = Color(0xff0e2e6b),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        //EventTitle(event.eventTitle)
                        EditableField(
                            value = communityUiState.eventTitle,
                            onValueChange = {communityViewModel.updateEventTitle(it)},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Log.d("Edit Screen",communityUiState.eventTitle)
                        Title(
                            title = "Details:",
                            color = Color(0xff0e2e6b),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        //EventDetails(event.EventDetails)
                        EditableField(
                            value = communityUiState.eventDetails,
                            onValueChange = {communityViewModel.updateEventDetails(it)},
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                ButtonAtBottom(
                    onClick = {
                        if (communityViewModel.validateEditForm()) {
                            communityViewModel.updateEventToFirebase()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = communityUiState.errorMessage!!,
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
fun Title(
    title: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = TextStyle(
            color = color,
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsFontFamily,
            fontSize = 20.sp
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun EventPictureEditable(
    eventImg: String,
    communityViewModel: CommunityViewModel
) {
    val context = LocalContext.current
    val uiState by communityViewModel.uiState.collectAsStateWithLifecycle()

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

                    communityViewModel.updateEventImg(base64String)
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
        Title(
            color = Color(0xff0e2e6b),
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Event Image:"
        )

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
                if (eventImg == "") {
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
                    val decodedBitmap = base64ToBitmap(eventImg)
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
        }
    }
}