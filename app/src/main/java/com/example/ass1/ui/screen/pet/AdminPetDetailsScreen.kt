package com.example.ass1.ui.screen.pet

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.component.base64ToBitmap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminPetDetailsScreen(
    onNavigateToPetList: () -> Unit,
    petViewModel: PetViewModel
) {

    val petUiState by petViewModel.uiState.collectAsStateWithLifecycle()
    petViewModel.getCurrentPetIndex(petUiState.selectedPet)

    val pet = petViewModel.petsList.value.find { it.petId == petUiState.selectedPet }
    petViewModel.assignPetInfoToCurrentPet()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (petUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        petUiState.isEdit,
        petUiState.isEditSuccessful,
        petUiState.isLoading
    ) {
        // Only proceed if donate was made and loading has finished
        if (petUiState.isEdit && !petUiState.isLoading) {
            if (petUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Pet details update successfully!",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                petViewModel.resetEditForm()
                onNavigateToPetList()
            } else {
                snackbarHostState.showSnackbar(
                    message = "Edit failed. Please try again.",
                    duration = SnackbarDuration.Short
                )
                petViewModel.resetEditForm()
                petViewModel.assignPetInfoToCurrentPet()
            }
        }
    }

    // Get screen dimensions to ensure proper sizing and positioning
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Calculate sizes relative to screen dimensions
    val petImageSize = (screenWidth * 0.45f).coerceAtMost(200.dp)
    val petThumbnailSize = (screenWidth * 0.35f).coerceAtMost(140.dp)
    val cardHeight = (screenHeight * 0.5f)
    //?
    val bottomBarHeight = 56.dp
    val topBarHeight = 56.dp
    val buttonHeight = 50.dp
    val buttonBottomPadding = 16.dp
    val headerHeight = 150.dp
    val petImageTopPadding = 60.dp
    val cardTopOffset = (-20).dp

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        contentWindowInsets = WindowInsets(0,0,0,0)
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFFE6F0F9),
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize()) {

            pet.let { currentPet ->

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
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = petImageTopPadding)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dog),
                                contentDescription = "Left pet icon",
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 20.dp)
                                    .offset(y = (-40).dp)
                                    .size((petImageSize * 1.3f).coerceAtMost(150.dp))
                            )
                            Image(
                                painter = painterResource(id = R.drawable.brown_minimalist),
                                contentDescription = "Right pet icon",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .padding(end = 32.dp)
                                    .size((petImageSize * 0.4f).coerceAtMost(80.dp))
                            )
                        }

                        // Details card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(cardHeight)
                                .padding(horizontal = 20.dp)
                                .offset(y = cardTopOffset),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 8.dp
                            )
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {

                                    Card(
                                        modifier = Modifier
                                            .size(petThumbnailSize),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(2.dp, Color.Black)
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            val decodedBitmap = base64ToBitmap(currentPet!!.image1)
                                            decodedBitmap?.let { bitmap ->
                                                Image(
                                                    bitmap = bitmap.asImageBitmap(),
                                                    contentDescription = currentPet.petName,
                                                    modifier = Modifier
                                                        .matchParentSize()
                                                        .padding(1.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }

                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(top = 16.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            TextField(
                                                value = petUiState.petName,
                                                onValueChange = { petViewModel.updatePetName(it) },
                                                modifier = Modifier.weight(1f),
                                                singleLine = true,
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent,
                                                    unfocusedContainerColor = Color.Transparent,
                                                    disabledContainerColor = Color.Transparent,
                                                ),
                                                placeholder = {
                                                    Text(
                                                        text = petViewModel.currentPet.value.petName,
                                                        style = TextStyle(
                                                            color = Color.Gray
                                                        )
                                                    )
                                                }
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit Name",
                                                tint = Color(0xFF333333)
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .padding(top = 16.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            TextField(
                                                value = petUiState.breed,
                                                onValueChange = { petViewModel.updatePetBreed(it) },
                                                modifier = Modifier.weight(1f),
                                                singleLine = true,
                                                label = {
                                                    Text(
                                                        text = "Breed",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                },
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent,
                                                    unfocusedContainerColor = Color.Transparent,
                                                    disabledContainerColor = Color.Transparent,
                                                ),
                                                placeholder = {
                                                    Text(
                                                        text = petViewModel.currentPet.value.breed,
                                                        style = TextStyle(
                                                            color = Color.Gray
                                                        )
                                                    )
                                                }
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit breed",
                                                tint = Color(0xFF333333)
                                            )
                                        }

                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 4.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            TextField(
                                                value = petUiState.editAge,
                                                onValueChange = { petViewModel.updatePetAge(it) },
                                                modifier = Modifier.weight(1f),
                                                singleLine = true,
                                                label = {
                                                    Text(
                                                        text = "Age",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                },
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = Color.Transparent,
                                                    unfocusedContainerColor = Color.Transparent,
                                                    disabledContainerColor = Color.Transparent,
                                                ),
                                                placeholder = {
                                                    Text(
                                                        text = petViewModel.currentPet.value.age.toString(),
                                                        style = TextStyle(
                                                            color = Color.Gray
                                                        )
                                                    )
                                                }
                                            )
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = "Edit age",
                                                tint = Color(0xFF333333)
                                            )
                                        }
                                    }
                                }

                                // ONLY THIS PART is scrollable
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(vertical = 16.dp)
                                ) {
                                    TextField(
                                        value = petUiState.deepDescription,
                                        onValueChange = { petViewModel.updatePetDeepDes(it) },
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState()),
                                        label = {
                                            Text(
                                                text = "Description",
                                                fontWeight = FontWeight.Bold
                                            )
                                        },
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            disabledContainerColor = Color.Transparent,
                                        ),
                                        placeholder = {
                                            Text(
                                                text = petViewModel.currentPet.value.deepDescription,
                                                style = TextStyle(
                                                    color = Color.Gray
                                                )
                                            )
                                        }
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit age",
                                        tint = Color(0xFF333333)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Surface(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .padding(bottom = buttonBottomPadding)
                                .fillMaxWidth()
                                .height(buttonHeight),
                            shape = RoundedCornerShape(20.dp),
                            shadowElevation = 10.dp,
                            color = Color(0xFF4A7AC7)
                        ) {
                            Button(
                                onClick = {
                                    if (petViewModel.validateEditForm()) {

                                        petViewModel.updateEditData()

                                    } else {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = petUiState.errorMessage!!,
                                                duration = SnackbarDuration.Short
                                            )
                                            petViewModel.resetEditForm()
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxSize(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A7AC7)),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "SAVE AND CHANGE",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 16.sp
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