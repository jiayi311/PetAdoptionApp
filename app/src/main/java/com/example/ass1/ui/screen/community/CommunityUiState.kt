package com.example.ass1.ui.screen.community

import com.example.ass1.model.Event
import com.google.firebase.Timestamp

data class CommunityUiState(
    val eventId: String = "",
    val eventTitle: String = "",
    val postedDate: Timestamp = Timestamp.now(),
    val eventDetails: String = "",
    val imageUrl: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val selectedEvent: String = "",
    val currentEvent: Event = Event(),
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false
)