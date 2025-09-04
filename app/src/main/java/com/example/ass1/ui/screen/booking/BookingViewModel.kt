package com.example.ass1.ui.screen.booking

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ass1.model.AdminBookingHistory
import com.example.ass1.model.Booking
import com.example.ass1.model.BookingDate
import com.example.ass1.ui.component.createTimestamp
import com.example.ass1.ui.screen.profile.ProfileViewModel
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
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BookingViewModel(
    private val profileViewModel: ProfileViewModel
): ViewModel() {

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val bookingsCollection = db.collection("Booking")

    private val _bookingCount = MutableStateFlow(0)
    private val _bookingList = MutableStateFlow<List<Booking>>(emptyList())
    val bookingList: MutableStateFlow<List<Booking>> = _bookingList

    private val _userBookingList = MutableStateFlow<List<Booking>>(emptyList())
    val userBookingList: StateFlow<List<Booking>> = _userBookingList.asStateFlow()

    private val _adminBookingList = MutableStateFlow<List<AdminBookingHistory>>(emptyList())
    val adminBookingList: StateFlow<List<AdminBookingHistory>> = _adminBookingList.asStateFlow()

    val reasonList = listOf("Meet a Pet", "Adoption Inquiry", "Adopt Pet")

    val bookingTimeList = listOf("10 AM", "11 AM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM")

    private val _bookingDateList = mutableStateOf<List<BookingDate>>(emptyList())
    val bookingDateList: List<BookingDate> get() = _bookingDateList.value


    init {
        listenForBookings()
        bookingHistoryList()
        _bookingDateList.value = getNextTwoWeeksDates()

        viewModelScope.launch {
            profileViewModel.currentUser.collect { user ->
                filterUserBookings(user.userId)
            }
        }
    }

    fun bookingHistoryList() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                // Create a temporary mutable list to hold results
                val tempAdminBookingList = mutableListOf<AdminBookingHistory>()

                // Process each booking sequentially to avoid race conditions
                for(bk in _bookingList.value) {
                    try {
                        val userDoc = db.collection("User")
                            .whereEqualTo("userId", bk.bookerId)
                            .get()
                            .await()

                        if (userDoc.documents.isNotEmpty()) {
                            val user = userDoc.documents[0]
                            // Extract the phone number from the user document
                            val phoneNumber = user.getString("userPhone") ?: ""

                            // Create AdminBookingHistory object from Booking
                            val adminBookingHistory = AdminBookingHistory(
                                booking = bk,
                                phone = phoneNumber
                            )

                            // Add to our temporary list
                            tempAdminBookingList.add(adminBookingHistory)
                        }
                    } catch (e: Exception) {
                        Log.e("Booking", "Error retrieving user info for booking ${bk.bookingId}", e)
                    }
                }

                // Update the StateFlow with the complete new list
                _adminBookingList.value = tempAdminBookingList

                // Set loading to false after all processing is complete
                _uiState.value = _uiState.value.copy(isLoading = false)

            } catch (e: Exception) {
                Log.e("Booking", "Error in bookingHistoryList", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun validateBookingForm(): Boolean {
        var returnValue: Boolean = true

        if (_uiState.value.selectedReason == "") {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Booking reason is required."
            )
            returnValue = false
        }
        return returnValue
    }

    fun updateTabState(newTab: Int) {
        _uiState.update { currentState ->
            currentState.copy(tabState = newTab)
        }
    }

    fun updateBookingToFirebaase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val updatedBooking = _uiState.value.selectedBooking
            try{
                // Query to find the document with this petId
                val querySnapshot = db.collection("Booking")
                    .whereEqualTo("bookingId", updatedBooking.bookingId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Booking").document(documentId)
                        .set(updatedBooking)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Booking not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update booking: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

    private fun convertBookingDateToTimestamp() {
        _uiState.value = _uiState.value.copy(
            bookMonth = convertMonthToInt(bookingDateList[_uiState.value.selectedDateIndex].month),
            bookDay = bookingDateList[_uiState.value.selectedDateIndex].day.toInt(),
            bookHour = convertSelectedTimeIndexToInt(_uiState.value.selectedTimeIndex),
        )
        val date = createTimestamp(
            day = _uiState.value.bookDay,
            month = _uiState.value.bookMonth,
            year = _uiState.value.bookYear,
            hour = _uiState.value.bookHour
        )
        _uiState.value = _uiState.value.copy(
            bookingDate = date
        )
    }

    fun updateSelectedBookingStatus(status: Int) {
        _uiState.value = _uiState.value.copy(
            selectedBooking = _uiState.value.selectedBooking.copy(status = status))
    }

    fun getSelectedBooking(booking: Booking) {
        _uiState.value = _uiState.value.copy(selectedBooking = booking)
    }

    fun convertMonthToInt(month: String): Int {
        return when (month.uppercase()) {
            "JANUARY", "JAN" -> 1
            "FEBRUARY", "FEB" -> 2
            "MARCH", "MAR" -> 3
            "APRIL", "APR" -> 4
            "MAY" -> 5
            "JUNE", "JUN" -> 6
            "JULY", "JUL" -> 7
            "AUGUST", "AUG" -> 8
            "SEPTEMBER", "SEP" -> 9
            "OCTOBER", "OCT" -> 10
            "NOVEMBER", "NOV" -> 11
            "DECEMBER", "DEC" -> 12
            else -> 0
        }
    }

    fun convertSelectedTimeIndexToInt(timeIndex: Int): Int {
        return when (timeIndex) {
            0 -> 10
            1 -> 11
            2 -> 13
            3 -> 14
            4 -> 15
            5 -> 16
            6 -> 17
            7 -> 18
            else -> 0
        }
    }

    fun resetBookingForm() {
        _uiState.value = _uiState.value.copy(
            bookerId = "",
            bookingId = "",
            bookingTime= Timestamp.now(),
            bookingDate = Timestamp.now(),
            bookDay = 0,
            bookMonth = 0,
            bookHour = 0,
            status = 0,
            isLoading = false,
            errorMessage = null,
            isBooking = false,
            isBookingSuccessful = false,
            selectedReason = "",
            selectedDateIndex = 0,
            selectedTimeIndex = 0,
            isEdit = false,
            isEditSuccessful = false
        )
    }

    fun updateBookingReason(reason: String) {
        _uiState.value = _uiState.value.copy(selectedReason = reason)
    }

    fun updateFilterDate(date: Long?) {
        _uiState.value = _uiState.value.copy(filterDate = date)
    }

    fun updateFilterReason(reason: String) {
        _uiState.value = _uiState.value.copy(filterReason = reason)
    }

    fun updateSelectedDateIndex(dateIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedDateIndex = dateIndex)
    }

    fun updateSelectedTimeIndex(timeIndex: Int) {
        _uiState.value = _uiState.value.copy(selectedTimeIndex = timeIndex)
    }

    private fun listenForBookings() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Booking")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for bookings: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val bookingList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Booking::class.java)
                    }
                    val sortedList = bookingList.sortedByDescending { it.bookingId }
                    _bookingList.value = sortedList
                    _bookingCount.value = _bookingList.value.size
                    filterUserBookings(profileViewModel.currentUser.value.userId)
                }
            }

    }

    fun getNextTwoWeeksDates(): List<BookingDate> {
        val dateList = mutableListOf<BookingDate>()
        val calendar = Calendar.getInstance()

        val dayFormat = SimpleDateFormat("dd", Locale.getDefault())

        val monthFormat = SimpleDateFormat("MMMM", Locale.getDefault())

        for (i in 0 until 15) {
            val date = calendar.time

            val day = dayFormat.format(date)
            val month = monthFormat.format(date)

            dateList.add(BookingDate(day, month))

            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        dateList.removeAt(0)

        return dateList
    }

    private fun autoGenerateBookingId(): String {
        val currentBookingCount = _bookingCount.value + 1
        _bookingCount.value = currentBookingCount
        val newBookingId = String.format(Locale.ROOT,"BK%03d", currentBookingCount)
        return newBookingId
    }

    fun submitNewBooking() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {

                val currentUser = profileViewModel.currentUser.value
                val currentUserId = currentUser.userId

                convertBookingDateToTimestamp()

                val bookingObj = Booking(
                    bookerId = currentUserId,
                    bookingId = autoGenerateBookingId(),
                    bookingTime = Timestamp.now(),
                    bookDate = _uiState.value.bookingDate,
                    bookingReason = _uiState.value.selectedReason,
                    status = 0
                )

                bookingsCollection.add(bookingObj).await()
                // Update UI state to show success
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isBooking = true,
                    isBookingSuccessful = true
                )
            } catch (e: Exception) {
                // Update UI state to show error
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isBooking = true,
                    isBookingSuccessful = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun filterUserBookings(userId: String) {
        // Add null check for userId
        if (userId.isBlank()) {
            return
        }

        // Create a new list with the filtered bookings
        val filteredList = _bookingList.value.filter { booking ->
            // Add null check for donation.donatorId
            val bookerId = booking.bookerId ?: ""
            val matches = bookerId == userId
            matches
        }

        _userBookingList.value = filteredList
    }

    fun filterBookings(
        bookings: List<AdminBookingHistory>,
        selectedDate: Long?,
        selectedReason: String
    ): List<AdminBookingHistory> {
        return bookings.filter { booking ->
            // Get the booking date as Timestamp
            val bookingTimestamp = booking.booking.bookDate as? Timestamp

            val sameDay = if (selectedDate != null && bookingTimestamp != null) {
                val selectedDateObj = Date(selectedDate)
                val bookingDateObj = bookingTimestamp.toDate()

                isSameDay(selectedDateObj, bookingDateObj)
            } else {
                true
            }

            sameDay && (selectedReason.isEmpty() || booking.booking.bookingReason == selectedReason)
        }
    }

    fun userFilterBookings(
        bookings: List<Booking>,
        selectedDate: Long?,
        selectedReason: String
    ): List<Booking> {
        return bookings.filter { booking ->
            // Get the booking date as Timestamp
            val bookingTimestamp = booking.bookDate as? Timestamp

            val sameDay = if (selectedDate != null && bookingTimestamp != null) {
                val selectedDateObj = Date(selectedDate)
                val bookingDateObj = bookingTimestamp.toDate()

                isSameDay(selectedDateObj, bookingDateObj)
            } else {
                true
            }

            sameDay && (selectedReason.isEmpty() || booking.bookingReason == selectedReason)
        }
    }

    fun getBookingStatusColor(status: Int): androidx.compose.ui.graphics.Color {
        return when(status) {
            0 -> Color(0xFFD5DFEB)  // Pending
            1 -> Color(0xFF469a8a)  // Approved
            2 -> Color(0xFFdc453a)  // Reject
            3 -> Color(0xFFdc453a)  // Cancelled
            else -> Color(0xFFD5DFEB)
        }
        // by default is pending status = 0
        // admin will set the approve = 1
        // and reject = 2
        // user can cancel booking = 3
    }

    fun getBookingStatusTextColor(status: Int): androidx.compose.ui.graphics.Color {
        return when(status) {
            0 -> Color.Black  // Pending
            else -> Color.White
        }
    }

    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        if (date1 == null || date2 == null) return false
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    fun convertMillisToDate(millis: Long): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    fun getFormattedDateFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun formatTimestampToTime(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("h a", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun getDayOfWeekFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val dayFormatter = SimpleDateFormat("EEE", Locale.ENGLISH)
        return dayFormatter.format(date)
    }

    @SuppressLint("NewApi")
    fun getTwoMonthsDateRange(): List<LocalDate> {
        val today = LocalDate.now()
        val startDate = today.minusMonths(1)
        val endDate = today.plusMonths(1)

        val dates = mutableListOf<LocalDate>()
        var current = startDate

        while (!current.isAfter(endDate)) {
            dates.add(current)
            current = current.plusDays(1)
        }

        return dates
    }

    class BookingViewModelFactory(
        private val profileViewModel: ProfileViewModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BookingViewModel(profileViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}