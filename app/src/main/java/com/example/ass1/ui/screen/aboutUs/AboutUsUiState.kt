package com.example.ass1.ui.screen.aboutUs

data class AboutUsUiState(
    val showContactDialog: Boolean = false,
    val errorMessage: String? = null,
    val isEdit: Boolean = false,
    val isLoading: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val mission: String = "",
    val operationHour: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val address: String = "",
)