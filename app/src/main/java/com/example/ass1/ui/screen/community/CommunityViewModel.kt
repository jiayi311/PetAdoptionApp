package com.example.ass1.ui.screen.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ass1.model.AboutUs
import com.example.ass1.model.Donation
import com.example.ass1.model.Event
import com.example.ass1.ui.screen.donation.DonationUiState
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class CommunityViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState: StateFlow<CommunityUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val eventCollection = db.collection("Event")

    private val _eventCount = MutableStateFlow(0)
    private val _eventsList = MutableStateFlow<List<Event>>(emptyList())
    val eventsList: MutableStateFlow<List<Event>> = _eventsList

    init {
        listenForEvents()
    }

    fun assignCurrentEventToUiState() {
        _uiState.value = _uiState.value.copy(
            eventTitle = _uiState.value.currentEvent.eventTitle,
            eventDetails = _uiState.value.currentEvent.eventDetails,
            imageUrl = _uiState.value.currentEvent.imageUrl,
        )
    }

    private fun autoGenerateEventId(): String {
        val currentEventCount = _eventCount.value + 1
        _eventCount.value = currentEventCount
        val newDonationId = String.format(Locale.ROOT,"EV%03d", currentEventCount)
        return newDonationId
    }

    fun submitNewEvent() {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val eventObj = Event(
                    eventTitle = _uiState.value.eventTitle,
                    eventDetails = _uiState.value.eventDetails,
                    eventId = autoGenerateEventId(),
                    imageUrl = _uiState.value.imageUrl,
                    postedDate = Timestamp.now()
                )

                eventCollection.add(eventObj).await()

                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = true
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = false,
                    errorMessage = "Failed to update post: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true,
                )
            }
        }
    }

    fun validateEventForm(): Boolean {
        var returnValue: Boolean = true

        if(_uiState.value.eventTitle.isBlank() || _uiState.value.eventDetails.isBlank() || _uiState.value.imageUrl.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "All fields is required."
            )
            returnValue = false
        }

        return returnValue
    }

    fun validateEditForm(): Boolean {
        var returnValue: Boolean = true

        val title = _uiState.value.eventTitle
        val details = _uiState.value.eventDetails
        val imageUrl = _uiState.value.imageUrl

        if(
            (title == _uiState.value.currentEvent.eventTitle) &&
            (details == _uiState.value.currentEvent.eventDetails) &&
            (imageUrl == _uiState.value.currentEvent.imageUrl)
        ) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Cannot save without new information"
            )
            returnValue = false
        }

        return returnValue
    }

    fun resetEditForm() {
        _uiState.value = _uiState.value.copy(
            eventId = "",
            eventTitle = "",
            postedDate = Timestamp.now(),
            eventDetails = "",
            imageUrl = "",
            errorMessage = null,
            isLoading = false,
            //selectedEvent = "",
            //currentEvent = Event(),
            isEdit = false,
            isEditSuccessful = false
        )
    }

    fun updateEventToFirebase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            var updateEv = Event()
            updateEv = updateEv.copy(
                eventId = _uiState.value.currentEvent.eventId,
                postedDate = _uiState.value.currentEvent.postedDate
            )
            if(_uiState.value.eventTitle !=  _uiState.value.currentEvent.eventTitle && _uiState.value.eventTitle.isNotBlank()) {
                updateEv.eventTitle = _uiState.value.eventTitle
            }else {
                updateEv.eventTitle = _uiState.value.currentEvent.eventTitle
            }

            if(_uiState.value.eventDetails !=  _uiState.value.currentEvent.eventDetails && _uiState.value.eventDetails.isNotBlank()) {
                updateEv.eventDetails = _uiState.value.eventDetails
            }else {
                updateEv.eventDetails =  _uiState.value.currentEvent.eventDetails
            }

            if(_uiState.value.imageUrl !=  _uiState.value.currentEvent.imageUrl && _uiState.value.imageUrl.isNotBlank()) {
                updateEv.imageUrl = _uiState.value.imageUrl
            }else {
                updateEv.imageUrl = _uiState.value.currentEvent.imageUrl
            }

            try{
                // Query to find the document with this report id
                val querySnapshot = db.collection("Event")
                    .whereEqualTo("eventId", updateEv.eventId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Event").document(documentId)
                        .set(updateEv)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Event not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update event: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

    fun updateSelectedEvent(eventId: String) {
        _uiState.value = _uiState.value.copy(
            selectedEvent = eventId
        )
    }

    fun updateEventTitle(title: String) {
        _uiState.value = _uiState.value.copy(
           eventTitle = title
        )
    }

    fun updateEventDetails(details: String) {
        _uiState.value = _uiState.value.copy(
            eventDetails = details
        )
    }

    fun getCurrentEventId(): String {
        return _uiState.value.selectedEvent
    }

    fun updateEventImg(img: String) {
        _uiState.value = _uiState.value.copy(
            imageUrl = img
        )
    }

    fun updateCurrentEventDetails(details: String) {
        _uiState.value = _uiState.value.copy(
            currentEvent = _uiState.value.currentEvent.copy(
                eventDetails = details
            )
        )
    }

    fun updateCurrentEventTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            currentEvent = _uiState.value.currentEvent.copy(
                eventTitle = title
            )
        )
    }

    fun updateCurrentEvent(event: Event) {
        _uiState.value = _uiState.value.copy(
            currentEvent = event
        )
    }

    private fun listenForEvents() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Event")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for events: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val eventsList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Event::class.java)
                    }
                    val sortedList = eventsList.sortedByDescending { it.eventId }
                    _eventsList.value = sortedList
                    _eventCount.value = _eventsList.value.size
                }
            }
    }

    fun getFormattedDateFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }
}