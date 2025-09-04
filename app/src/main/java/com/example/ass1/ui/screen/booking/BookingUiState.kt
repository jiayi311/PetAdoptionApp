package com.example.ass1.ui.screen.booking

import com.example.ass1.model.Booking
import com.example.ass1.model.BookingDate
import com.google.firebase.Timestamp

data class BookingUiState(
    val tabState: Int = 0,
    val bookerId: String = "",
    val bookingId: String = "",
    val bookingTime:Timestamp = Timestamp.now(),
    val bookingDate:Timestamp = Timestamp.now(),
    val bookDay: Int = 0,
    val bookMonth: Int = 0,
    val bookYear: Int = 2025,
    val bookHour: Int = 0,
    val status: Int = 0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isBooking: Boolean = false,
    val isBookingSuccessful: Boolean = false,
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val selectedReason: String = "",
    val selectedDateIndex: Int = 0,
    val selectedTimeIndex: Int = 0,
    val selectedBooking: Booking = Booking(),
    val filterDate: Long? = null,
    val filterReason: String = "",
)


