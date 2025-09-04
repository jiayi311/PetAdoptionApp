package com.example.ass1.ui.screen.pet

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.ui.component.base64ToBitmap
import com.example.ass1.ui.theme.poppinsFontFamily
import android.content.res.Configuration

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PetDetailsScreen(
    petViewModel: PetViewModel,
    onNavigateToBooking: () -> Unit
) {

    val petUiState by petViewModel.uiState.collectAsStateWithLifecycle()
    petViewModel.getCurrentPetIndex(petUiState.selectedPet)

    val pet = petViewModel.petsList.value.find { it.petId == petUiState.selectedPet }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Device type detection
    val isTablet = screenWidth > 600.dp

    // Use the smaller dimension for sizing to ensure consistent proportions
    val smallerDimension = minOf(screenWidth, screenHeight)

    // Dynamic sizing based on device type
    val petImageSize = if (isTablet) {
        (smallerDimension * 0.35f).coerceAtMost(350.dp) // Slightly smaller relative size on tablets
    } else {
        (smallerDimension * 0.3f).coerceAtMost(280.dp)
    }

    val petThumbnailSize = if (isTablet) {
        (smallerDimension * 0.28f).coerceAtMost(280.dp) // Larger absolute size for tablets
    } else {
        (smallerDimension * 0.32f).coerceAtMost(200.dp)
    }

    // Dynamic text sizing
    val titleFontSize = if (isTablet) 26.sp else 20.sp
    val bodyFontSize = if (isTablet) 18.sp else 16.sp
    val descriptionFontSize = if (isTablet) 16.sp else 14.sp
    val buttonFontSize = if (isTablet) 18.sp else 16.sp

    // Dynamic padding and spacing
    val standardPadding = if (isTablet) 24.dp else 16.dp
    val smallPadding = if (isTablet) 12.dp else 8.dp
    val largePadding = if (isTablet) 32.dp else 20.dp

    // Adjust card height based on orientation and device type
    val cardHeight = when {
        isTablet && isLandscape -> screenHeight * 0.7f
        isTablet -> screenHeight * 0.45f
        isLandscape -> screenHeight * 0.75f
        else -> screenHeight * 0.5f
    }

    // Dynamic arrow and button sizes
    val arrowIconSize = if (isTablet) {
        (smallerDimension * 0.06f).coerceAtMost(36.dp).coerceAtLeast(28.dp)
    } else {
        (smallerDimension * 0.07f).coerceAtMost(28.dp).coerceAtLeast(24.dp)
    }

    val arrowButtonSize = if (isTablet) {
        (smallerDimension * 0.1f).coerceAtMost(72.dp).coerceAtLeast(56.dp)
    } else {
        (smallerDimension * 0.12f).coerceAtMost(56.dp).coerceAtLeast(48.dp)
    }

    val buttonHeight = if (isTablet) {
        (smallerDimension * 0.08f).coerceAtMost(65.dp).coerceAtLeast(50.dp)
    } else {
        (smallerDimension * 0.12f).coerceAtMost(50.dp).coerceAtLeast(40.dp)
    }

    val cardTopOffset = if (isTablet) (-25).dp else (-20).dp

    Box(modifier = Modifier.fillMaxSize()) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Adjust top row layout for orientation and device type
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = if (isLandscape) smallPadding else standardPadding,
                                bottom = if (isLandscape) smallPadding else standardPadding,
                                start = largePadding,
                                end = largePadding
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dog),
                            contentDescription = "Left pet icon",
                            modifier = Modifier
                                .size((petImageSize * 1.3f).coerceAtMost(if (isTablet) 200.dp else 150.dp))
                        )
                        Image(
                            painter = painterResource(id = R.drawable.brown_minimalist),
                            contentDescription = "Right pet icon",
                            modifier = Modifier
                                .size((petImageSize * 0.4f).coerceAtMost(if (isTablet) 100.dp else 80.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(if (isLandscape) smallPadding else standardPadding))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(cardHeight)
                            .padding(horizontal = largePadding)
                            .offset(y = cardTopOffset),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isTablet) 10.dp else 8.dp
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(standardPadding)
                        ) {
                            // Adapt the layout based on orientation
                            if (isLandscape) {
                                // Landscape layout - different arrangement
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Left side - pet image
                                    Card(
                                        modifier = Modifier
                                            .size(petThumbnailSize)
                                            .padding(end = standardPadding),
                                        shape = RoundedCornerShape(18.dp),
                                        border = BorderStroke(2.dp, Color.Black)
                                    ) {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            val decodedBitmap = pet?.let { base64ToBitmap(it.image1) }
                                            decodedBitmap?.let { bitmap ->
                                                Image(
                                                    bitmap = bitmap.asImageBitmap(),
                                                    contentDescription = pet?.petName ?: "",
                                                    modifier = Modifier
                                                        .matchParentSize()
                                                        .padding(1.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }

                                    // Right side - pet details and description
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    ) {
                                        // Pet name and basic info
                                        Text(
                                            text = pet?.petName ?: "",
                                            modifier = Modifier.fillMaxWidth(),
                                            fontSize = titleFontSize,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif,
                                            color = Color(0xFF333333),
                                        )

                                        Text(
                                            text = "Breed: ${pet?.breed ?: ""}",
                                            fontSize = bodyFontSize,
                                            fontFamily = poppinsFontFamily
                                        )

                                        Text(
                                            text = "Age: ${pet?.age ?: ""}",
                                            fontSize = bodyFontSize,
                                            fontFamily = poppinsFontFamily
                                        )

                                        // Description in a scrollable box
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f)
                                                .padding(vertical = smallPadding)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .verticalScroll(rememberScrollState())
                                            ) {
                                                Text(
                                                    text = "Description: ${pet?.deepDescription ?: ""}",
                                                    fontSize = descriptionFontSize,
                                                    lineHeight = if (isTablet) 28.sp else 24.sp,
                                                    textAlign = TextAlign.Left,
                                                    fontFamily = poppinsFontFamily
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                // Portrait layout - original design with tablet adjustments
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
                                            val decodedBitmap = pet?.let { base64ToBitmap(it.image1) }
                                            decodedBitmap?.let { bitmap ->
                                                Image(
                                                    bitmap = bitmap.asImageBitmap(),
                                                    contentDescription = pet?.petName ?: "",
                                                    modifier = Modifier
                                                        .matchParentSize()
                                                        .padding(1.dp),
                                                    contentScale = ContentScale.Crop
                                                )
                                            }
                                        }
                                    }

                                    // Pet details on right
                                    Column(
                                        modifier = Modifier
                                            .padding(start = standardPadding)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = pet?.petName ?: "",
                                            modifier = Modifier
                                                .padding(top = standardPadding)
                                                .fillMaxWidth(),
                                            fontSize = titleFontSize,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.SansSerif,
                                            textAlign = TextAlign.Left,
                                            color = Color(0xFF333333),
                                        )
                                        Text(
                                            text = "Breed: ${pet?.breed ?: ""}",
                                            modifier = Modifier
                                                .padding(top = standardPadding)
                                                .fillMaxWidth(),
                                            fontSize = bodyFontSize,
                                            lineHeight = if (isTablet) 28.sp else 24.sp,
                                            fontFamily = poppinsFontFamily
                                        )
                                        Text(
                                            text = "Age: ${pet?.age ?: ""}",
                                            fontSize = bodyFontSize,
                                            lineHeight = if (isTablet) 28.sp else 24.sp,
                                            modifier = Modifier.padding(vertical = smallPadding),
                                            fontFamily = poppinsFontFamily
                                        )
                                    }
                                }

                                // Description section - scrollable
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(vertical = standardPadding)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Text(
                                            text = "Description: ${pet?.deepDescription ?: ""}",
                                            fontSize = descriptionFontSize,
                                            lineHeight = if (isTablet) 28.sp else 24.sp,
                                            textAlign = TextAlign.Left,
                                            fontFamily = poppinsFontFamily
                                        )
                                    }
                                }
                            }

                            // Navigation arrows with consistent sizes
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = if (isLandscape) smallPadding else standardPadding),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Left arrow with fixed size
                                IconButton(
                                    onClick = { petViewModel.navigateToPreviousPet() },
                                    modifier = Modifier.size(arrowButtonSize)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Previous pet",
                                        modifier = Modifier.size(arrowIconSize),
                                        tint = Color.Black
                                    )
                                }

                                // Right arrow with fixed size
                                IconButton(
                                    onClick = { petViewModel.navigateToNextPet() },
                                    modifier = Modifier.size(arrowButtonSize)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next pet",
                                        modifier = Modifier.size(arrowIconSize),
                                        tint = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(if (isLandscape) smallPadding else standardPadding))

                    Button(
                        onClick = { onNavigateToBooking() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(buttonHeight)
                            .padding(horizontal = largePadding),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00348E)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "ADOPT ME",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = buttonFontSize,
                            fontFamily = poppinsFontFamily
                        )
                    }

                    Spacer(modifier = Modifier.height(if (isLandscape) smallPadding else standardPadding))
                }
            }
        }
    }
}