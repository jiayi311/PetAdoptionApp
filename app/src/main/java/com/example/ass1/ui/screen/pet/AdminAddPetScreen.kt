package com.example.ass1.ui.screen.pet

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.component.bitmapToBase64
import java.io.InputStream

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminAddPetScreen(
    onNavigateToPetList: () -> Unit,
    petViewModel: PetViewModel
) {
    val petUiState by petViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Detect if device is a tablet based on screen size
    val isTablet = configuration.screenWidthDp >= 600

    // Track if we're currently adding a pet
    var isAddingPet by remember { mutableStateOf(false) }

    // Reset the form only when first entering the screen, not on rotation
    LaunchedEffect(Unit) {
        // Only reset if there's no data (first time entering)
        if (petUiState.newPetName.isEmpty() && petUiState.newPetBreed.isEmpty() &&
            petUiState.newPetAge.isEmpty() && petUiState.newPetDescription.isEmpty()) {
            petViewModel.resetEditForm()
        }
    }

    // Watch for add pet operation completion
    LaunchedEffect(petUiState.isAddPetSuccess, petUiState.isLoading) {
        if (isAddingPet && !petUiState.isLoading) {
            if (petUiState.isAddPetSuccess) {
                snackbarHostState.showSnackbar(
                    message = "Pet added successfully!",
                    duration = SnackbarDuration.Short
                )
                delay(500)
                petViewModel.resetEditForm()
                onNavigateToPetList()
            } else {
                snackbarHostState.showSnackbar(
                    message = petUiState.errorMessage ?: "Failed to add pet. Please try again.",
                    duration = SnackbarDuration.Short
                )
                isAddingPet = false
            }
        }
    }

    if (petUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val context = LocalContext.current
    val uiState by petViewModel.uiState.collectAsStateWithLifecycle()

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

                   petViewModel.updateNewPetImage1(base64String)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFE6F0F9))
        ) {
            // Header background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF4A7AC7))
                    .align(Alignment.TopCenter)
            )

            // Content layout depends on orientation
            if (isLandscape) {
                // Landscape layout
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Left side - Dog image
                    Box(
                        modifier = Modifier
                            .weight(0.3f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dog),
                            contentDescription = "Pet image",
                            modifier = Modifier
                                .size(180.dp)
                                .offset(y = (-20).dp)
                        )
                    }

                    // Right side - Form card
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxHeight()
                    ) {
                        // Pet form card
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                // Left side with image upload
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .weight(0.3f)
                                        .padding(end = 16.dp)
                                ) {
                                    // Image placeholder
                                    Card(
                                        modifier = Modifier.size(120.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(2.dp, Color.Gray)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFEEEEEE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (petUiState.newPetImage1.isNotEmpty()) {
                                                Text(
                                                    "Image Selected",
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 14.sp
                                                )
                                            } else {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Image,
                                                        contentDescription = "Upload image",
                                                        modifier = Modifier.size(48.dp),
                                                        tint = Color.Gray
                                                    )
                                                    Text(
                                                        "Tap to upload",
                                                        color = Color.Gray,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }

                                            IconButton(
                                                onClick = {
                                                    imagePickerLauncher.launch("image/*")
                                                },
                                                modifier = Modifier.fillMaxSize()
                                            ) {}
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Add Pet Button in landscape
                                    Button(
                                        onClick = {
                                            if (petViewModel.validateAddPetForm()) {
                                                isAddingPet = true
                                                petViewModel.addNewPet()
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = petUiState.errorMessage
                                                            ?: "Please fill in all required fields",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4A7AC7)
                                        ),
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Text(
                                            text = "ADD PET",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontSize = 16.sp
                                        )
                                    }
                                }

                                // Right side with form fields
                                Column(
                                    modifier = Modifier
                                        .weight(0.7f)
                                        .fillMaxHeight()
                                ) {
                                    // Name and Breed in first row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                    ) {
                                        // Name field
                                        TextField(
                                            value = petUiState.newPetName,
                                            onValueChange = { petViewModel.updateNewPetName(it) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 4.dp),
                                            singleLine = true,
                                            label = { Text("Pet Name") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )

                                        // Breed field
                                        TextField(
                                            value = petUiState.newPetBreed,
                                            onValueChange = { petViewModel.updateNewPetBreed(it) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 4.dp),
                                            singleLine = true,
                                            label = { Text("Breed") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )
                                    }

                                    // Age and Short Description in second row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                    ) {
                                        // Age field
                                        TextField(
                                            value = petUiState.newPetAge,
                                            onValueChange = { petViewModel.updateNewPetAge(it) },
                                            modifier = Modifier
                                                .weight(0.4f)
                                                .padding(end = 4.dp),
                                            singleLine = true,
                                            label = { Text("Age") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )

                                        // Short Description field
                                        TextField(
                                            value = petUiState.newPetShortDescription,
                                            onValueChange = { petViewModel.updateNewPetShortDescription(it) },
                                            modifier = Modifier
                                                .weight(0.6f)
                                                .padding(start = 4.dp),
                                            singleLine = true,
                                            label = { Text("Short Description") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )
                                    }

                                    // Description field in third row
                                    TextField(
                                        value = petUiState.newPetDescription,
                                        onValueChange = { petViewModel.updateNewPetDescription(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f),  // Take remaining space
                                        label = { Text("Description") },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFFF5F5F5),
                                            unfocusedContainerColor = Color(0xFFF0F0F0),
                                            focusedLabelColor = Color(0xFF4A7AC7),
                                            unfocusedLabelColor = Color(0xFF666666)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Portrait layout
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
                            .padding(top = 16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        
                        // Pet images section
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dog),
                                contentDescription = "Dog image",
                                modifier = Modifier
                                    .size(160.dp)
                            )

                            Image(
                                painter = painterResource(id = R.drawable.brown_minimalist),
                                contentDescription = "Cat image",
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(end = 16.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        // Pet form card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                // Image and basic info row
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                ) {
                                    // Image upload card
                                    Card(
                                        modifier = Modifier.size(100.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(2.dp, Color.Gray)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFEEEEEE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (petUiState.newPetImage1.isNotEmpty()) {
                                                Text(
                                                    "Image Selected",
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 14.sp
                                                )
                                            } else {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Image,
                                                        contentDescription = "Upload image",
                                                        modifier = Modifier.size(36.dp),
                                                        tint = Color.Gray
                                                    )
                                                    Text(
                                                        "Tap to upload",
                                                        color = Color.Gray,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }

                                            IconButton(
                                                onClick = {
                                                    imagePickerLauncher.launch("image/*")
                                                },
                                                modifier = Modifier.fillMaxSize()
                                            ) {}
                                        }
                                    }

                                    // Basic pet info
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 16.dp)
                                    ) {
                                        // Name field
                                        TextField(
                                            value = petUiState.newPetName,
                                            onValueChange = { petViewModel.updateNewPetName(it) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            label = { Text("Pet Name") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Breed field
                                        TextField(
                                            value = petUiState.newPetBreed,
                                            onValueChange = { petViewModel.updateNewPetBreed(it) },
                                            modifier = Modifier.fillMaxWidth(),
                                            singleLine = true,
                                            label = { Text("Breed") },
                                            colors = TextFieldDefaults.colors(
                                                focusedContainerColor = Color(0xFFF5F5F5),
                                                unfocusedContainerColor = Color(0xFFF0F0F0),
                                                focusedLabelColor = Color(0xFF4A7AC7),
                                                unfocusedLabelColor = Color(0xFF666666)
                                            )
                                        )
                                    }
                                }

                                // Row for Age and Short Description
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                ) {
                                    // Age field
                                    TextField(
                                        value = petUiState.newPetAge,
                                        onValueChange = { petViewModel.updateNewPetAge(it) },
                                        modifier = Modifier
                                            .weight(0.4f)
                                            .padding(end = 8.dp),
                                        singleLine = true,
                                        label = { Text("Age") },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFFF5F5F5),
                                            unfocusedContainerColor = Color(0xFFF0F0F0),
                                            focusedLabelColor = Color(0xFF4A7AC7),
                                            unfocusedLabelColor = Color(0xFF666666)
                                        )
                                    )

                                    // Short Description field
                                    TextField(
                                        value = petUiState.newPetShortDescription,
                                        onValueChange = {
                                            petViewModel.updateNewPetShortDescription(
                                                it
                                            )
                                        },
                                        modifier = Modifier
                                            .weight(0.6f)
                                            .padding(start = 8.dp),
                                        singleLine = true,
                                        label = { Text("Short Description") },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color(0xFFF5F5F5),
                                            unfocusedContainerColor = Color(0xFFF0F0F0),
                                            focusedLabelColor = Color(0xFF4A7AC7),
                                            unfocusedLabelColor = Color(0xFF666666)
                                        )
                                    )
                                }

                                // Description Field - ensure it has enough height
                                TextField(
                                    value = petUiState.newPetDescription,
                                    onValueChange = { petViewModel.updateNewPetDescription(it) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)  // This will make it take up available space
                                        .padding(bottom = 16.dp),
                                    label = { Text("Description") },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFFF5F5F5),
                                        unfocusedContainerColor = Color(0xFFF0F0F0),
                                        focusedLabelColor = Color(0xFF4A7AC7),
                                        unfocusedLabelColor = Color(0xFF666666)
                                    )
                                )

                                // Add Pet Button
                                Button(
                                    onClick = {
                                        if (petViewModel.validateAddPetForm()) {
                                            isAddingPet = true
                                            petViewModel.addNewPet()
                                        } else {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = petUiState.errorMessage
                                                        ?: "Please fill in all required fields",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4A7AC7)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = "ADD PET",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}