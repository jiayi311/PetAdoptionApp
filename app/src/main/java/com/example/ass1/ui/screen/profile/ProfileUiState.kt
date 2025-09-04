package com.example.ass1.ui.screen.profile

import androidx.compose.ui.text.input.TextFieldValue

data class ProfileUiState(
    val userId: String = "",
    val userName: String = "",
    val userMail: String = "",
    val userPhone: String = "",
    val userPhoneUi: TextFieldValue = TextFieldValue(""),
    val userGender: Int = 0,
    val errorMessage: String? = null,
    val phoneErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val isChangePw: Boolean = false,
    val isChangePwSuccessful: Boolean = false
)