package com.example.ass1.ui.screen.loginRegister

import androidx.compose.ui.text.input.TextFieldValue

data class LoginRegisterUiState(
    val userId:String = "",
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val userPhoneUi: TextFieldValue = TextFieldValue(""),
    val userGender: Int = 0,
    val errorMessage: String? = null,
    val pwErrorMessage: String? = null,
    val phoneErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isRegister: Boolean = false,
    val isLogin: Boolean = false,
    val isResetPw: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isResetPwSuccessful: Boolean = false,
    val passwordVisible: Boolean = false,
    val userPw: String = "",
    val confPw: String = "",
    val isAdmin: Boolean = false
)

