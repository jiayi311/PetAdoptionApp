package com.example.ass1.ui.screen.pet

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminPetListScreen(
    onNavigateToPetDetails: () -> Unit,
    onNavigateToAdminAddPet: () -> Unit, // New parameter for navigation
    petViewModel: PetViewModel,
) {
    val configuration = LocalConfiguration.current
    val petUiState by petViewModel.uiState.collectAsStateWithLifecycle()

    val searchQuery = petUiState.searchQuery
    val pets = petViewModel.petsListShow.collectAsStateWithLifecycle()
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLandscape) {
            // Landscape layout
            PetListLandscape(
                searchQuery = searchQuery,
                onSearchQueryChange = { petViewModel.updateSearchQuery(it) },
                pets = pets.value,
                onPetSelected = { pet ->
                    petViewModel.updateSelectedPet(petId = pet.petId)
                    onNavigateToPetDetails()
                }
            )
        } else {
            // Original portrait layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { petViewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pets.value) { pet ->
                        PetCard(pet = pet, onViewMoreClick = {
                            petViewModel.updateSelectedPet(petId = pet.petId)
                            onNavigateToPetDetails()
                        })
                    }
                }
            }
        }

        // Add floating action button for navigation to AdminAddPet screen
        FloatingActionButton(
            onClick = {
                Log.d("PetListScreen", "Add Pet FAB clicked")
                onNavigateToAdminAddPet()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(56.dp),
            containerColor = Color(0xFF70A9FF),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Pet",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}