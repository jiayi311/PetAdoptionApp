package com.example.ass1.ui.screen.community

import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.ui.screen.aboutUs.EditableField
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AdminCommunityAddScreen(
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
        if (communityUiState.isEdit && !communityUiState.isLoading) {
            if (communityUiState.isEditSuccessful) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Add post successfully!",
                        duration = SnackbarDuration.Short
                    )

                    delay(200)
                    onNavigateToCommunity()
                    communityViewModel.resetEditForm()
                }
            } else {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = "Failed to adding post. Please try again.",
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
                        //Choose image from gallery
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

                        Title(
                            title = "Details:",
                            color = Color(0xff0e2e6b),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        //EventDetails(event.EventDetails)
                        EditableField(
                            value = communityUiState.eventDetails,
                            onValueChange = {communityViewModel.updateEventDetails(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                ButtonAtBottom(
                    onClick = {
                        if (communityViewModel.validateEventForm()) {
                                communityViewModel.submitNewEvent()
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = communityUiState.errorMessage!!,
                                    duration = SnackbarDuration.Short
                                )
                                communityViewModel.resetEditForm()
                            }
                        }
                    },
                    text = "Add Event"
                )
            }
        }
    }
}