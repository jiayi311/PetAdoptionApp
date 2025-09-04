package com.example.ass1.ui.screen.donation

import com.example.ass1.model.ExpenseItem
import com.example.ass1.model.Receipt
import com.example.ass1.model.ReceiptItem
import com.google.firebase.Timestamp

data class DonationUiState(
    val donatorId: String = "",
    val donationId: String = "",
    val donateTime: Timestamp = Timestamp.now(),
    val donateAmount: Double = 0.0,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isDonate: Boolean = false,
    val isDonationSuccessful: Boolean = false,
    val tabState: Int = 0,
    val selectedButton: Int = 0,
    val customAmount: Double = 0.0,
    val selectedExpenses: ExpenseItem = ExpenseItem(),
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val imageName: String = "",
    val newExpenses: ExpenseItem = ExpenseItem(),
    val newReceiptItemList: List<ReceiptItem> = List(5) { ReceiptItem() },
    val selectedDate: Long = 0,
    val selectedTime: Long = 0,
)


