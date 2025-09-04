package com.example.ass1.ui.screen.loginRegister

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ass1.data.repository.UserRepository
import com.example.ass1.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class LoginRegisterViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()
    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Initial)

    //forFirebase
    private val db = Firebase.firestore
    private val usersCollection = db.collection("User")
    private var auth: FirebaseAuth =  FirebaseAuth.getInstance()

    private val _userCount = MutableStateFlow(0)
    private val _usersList = MutableStateFlow<List<User>>(emptyList())

    init {
       listenForUsers()
    }

    fun resetIsRegister() {
        _uiState.value = _uiState.value.copy(isRegister = false)
    }

    fun resetIsResetPw() {
        _uiState.value = _uiState.value.copy(isResetPw = false)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(userEmail = email)
    }

    fun updatePwVisibility(passwordVisible: Boolean) {
        _uiState.value = _uiState.value.copy(passwordVisible = passwordVisible)
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(userName = name)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        // Apply formatting and validate max length
        val formattedPhone = formatPhoneNumber(phoneNumber)
        val digitsOnly = formattedPhone.filter { it.isDigit() }

        // Check if it starts with "011" to determine max length
        val isFormat011 = digitsOnly.take(3) == "011"
        val maxDigits = if (isFormat011) 11 else 10

        // Only update if within valid length
        if (digitsOnly.length <= maxDigits) {
            _uiState.value = _uiState.value.copy(
                userPhone = formattedPhone,
                userPhoneUi = TextFieldValue(
                    text = formattedPhone,
                    selection = TextRange(formattedPhone.length) // Cursor at the end
                ),
            )
        }
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(userPw = password)
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.value = _uiState.value.copy(confPw = confirmPassword)
    }

    fun resetLoginRegisterForm() {
        _uiState.update { currentState ->
            currentState.copy(
                userId = "",
                userName = "",
                userEmail = "",
                userPhone = "",
                userPhoneUi = TextFieldValue(""),
                userGender = 0,
                errorMessage  = null,
                isLoading = false,
                isRegister = false,
                isLogin = false,
                isResetPw = false,
                isRegistrationSuccessful  = false,
                isLoginSuccessful  = false,
                isResetPwSuccessful  = false,
                passwordVisible = false,
                userPw = "",
                confPw = "",
                isAdmin = false
            )
        }
    }

    fun createAccount() {

        val currentState = _uiState.value
        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(
                    currentState.userEmail,
                    currentState.userPw
                ).await()

                val userObj = User(
                    userId = autoGenerateUserId(),
                    userName = currentState.userName,
                    userMail = currentState.userEmail,
                    userPhone = currentState.userPhone,
                    userGender = 0
                )

                usersCollection.add(userObj).await()

                _uiState.value = currentState.copy(
                    isLoading = false,
                    isRegister = true,
                    isRegistrationSuccessful = true
                )

            } catch (e: Exception) {
                Log.d("",e.message!!)

                resetLoginRegisterForm()

                _uiState.value = currentState.copy(
                    isLoading = false,
                    isRegister = true,
                    errorMessage = e.message ?: "Registration failed",
                    isRegistrationSuccessful = false
                )
            }
        }
    }

    private fun checkIfAdmin(email: String): Boolean {
        return email.contains("admin")
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val authResult = auth.signInWithEmailAndPassword(email, password).await()

                if(checkIfAdmin(email)) {
                    _uiState.value = _uiState.value.copy(
                        isAdmin = true
                    )
                }

                if (authResult.user != null) {
                    userRepository.fetchUserByEmail(email)
                        .onSuccess { user ->
                            _uiState.value = _uiState.value.copy(
                                isLoginSuccessful = true,
                                isLogin = true
                            )
                        }
                        .onFailure { exception ->
                            _uiState.value = _uiState.value.copy(
                                errorMessage = exception.message ?: "Authentication failed",
                                isLogin = true
                            )
                        }
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Login Failed",
                    isLogin = true,
                    isLoading = false
                )
            }

        }
    }

    fun signOut() {
        viewModelScope.launch {
            auth.signOut()
            userRepository.clearUserData()
        }
    }

    fun validateLoginForm(
        email: String,
        password: String
    ):Boolean {

        var returnValue: Boolean = true

        if(email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Email and password should not be blank."
            )
            returnValue = false
        }else if(!email.contains("@")) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Please enter a valid email address."
                )
                returnValue = false
        }

        return returnValue
    }

    private fun autoGenerateUserId(): String {
        val currentUserCount = _userCount.value + 1
        _userCount.value = currentUserCount
        val newUserId = String.format(Locale.ROOT,"UR%03d", currentUserCount)
        return newUserId
    }

    fun passwordValidation(password: String): Boolean {

        var returnValue:Boolean = true

        if(password.length < 8) {
            _uiState.value = _uiState.value.copy(
                pwErrorMessage = "At least 8 characters"
            )
            returnValue = false
        }else if(!(password.any { it.isDigit() })) {
            _uiState.value = _uiState.value.copy(
                pwErrorMessage = "At least one number (0-9)"
            )
            returnValue = false
        }else if(!(password.any { it.isUpperCase() })) {
            _uiState.value = _uiState.value.copy(
                pwErrorMessage = "At least one uppercase letter (A-Z)"
            )
            returnValue = false
        }else if(!(password.any { it.isLowerCase() })) {
            _uiState.value = _uiState.value.copy(
                pwErrorMessage = "At least one lowercase letter (a-z)"
            )
            returnValue = false
        }else if(!(password.any { !it.isLetterOrDigit() })) {
            _uiState.value = _uiState.value.copy(
                pwErrorMessage = "At least one special character (@#_.~)"
            )
            returnValue = false
        }
        return returnValue
    }

    fun phoneValidation(phoneNumber: String): Boolean {
        // Format for 011-xxxx xxxx
        val regex011 = """^011-\d{4}\s\d{4}$""".toRegex()

        // Format for 01x-xxx xxxx (where x ≠ 1)
        val regex01x = """^01[2-9]-\d{3}\s\d{4}$""".toRegex()

        if((!regex011.matches(phoneNumber) || regex01x.matches(phoneNumber))) {
            _uiState.value = _uiState.value.copy(
                phoneErrorMessage = "Please use Malaysia's phone number."
            )
        }

        return regex011.matches(phoneNumber) || regex01x.matches(phoneNumber)
    }

    /**
     * Auto-formats the phone number to match:
     * - 01x-xxx xxxx (where x ≠ 1)
     * - 011-xxxx xxxx
     * as the user types
     */
    private fun formatPhoneNumber(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }

        // Check if it starts with "011"
        val isFormat011 = digitsOnly.take(3) == "011"

        return when {
            // Not enough digits to determine format yet
            digitsOnly.length <= 3 -> digitsOnly

            // Format for 011-xxxx xxxx
            isFormat011 -> {
                when {
                    digitsOnly.length <= 7 -> {
                        val part1 = digitsOnly.substring(0, 3)
                        val part2 = digitsOnly.substring(3, digitsOnly.length)
                        "$part1-$part2"
                    }
                    else -> {
                        val part1 = digitsOnly.substring(0, 3)
                        val part2 = digitsOnly.substring(3, 7)
                        val part3 = digitsOnly.substring(7, minOf(11, digitsOnly.length))
                        "$part1-$part2 $part3"
                    }
                }
            }

            // Format for 01x-xxx xxxx (where x ≠ 1)
            else -> {
                when {
                    digitsOnly.length <= 6 -> {
                        val part1 = digitsOnly.substring(0, 3)
                        val part2 = digitsOnly.substring(3, digitsOnly.length)
                        "$part1-$part2"
                    }
                    else -> {
                        val part1 = digitsOnly.substring(0, 3)
                        val part2 = digitsOnly.substring(3, 6)
                        val part3 = digitsOnly.substring(6, minOf(10, digitsOnly.length))
                        "$part1-$part2 $part3"
                    }
                }
            }
        }
    }

    fun validateRegisterForm(
        fullName: String,
        email: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var returnValue: Boolean = true

        if(email.isBlank() || password.isBlank() || fullName.isBlank()
            || phoneNumber.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "All fields are required."
            )
            returnValue = false
        }else if(!email.contains("@")) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid email address."
            )
            returnValue = false
        }else if(!phoneValidation(phoneNumber)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please follow the phone number format."
            )
            returnValue = false
        }else if(!passwordValidation(password)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please follow the password format."
            )
            returnValue = false
        }else if(password != confirmPassword) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Your password is not same."
            )
            returnValue = false
        }else if(fullName.any { it.isDigit() }) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Name should not contains any number! "
            )
            returnValue = false
        }

        return returnValue
    }

    fun validateResetPwForm(
        email: String,
    ): Boolean {
        var returnValue: Boolean = true

        if(email.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "All fields are required."
            )
            returnValue = false
        }else if(!checkEmailInFirebase(email)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "The email doesn't exist. Please enter a valid email address."
            )
            returnValue = false
        }

        return returnValue
    }

    private fun listenForUsers() {
        val db = FirebaseFirestore.getInstance()
        db.collection("User")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for users: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val usersList = snapshot.documents.mapNotNull { document ->
                        document.toObject(User::class.java)
                    }
                    _usersList.value = usersList
                    _userCount.value = _usersList.value.size
                }
            }
    }

    private fun checkEmailInFirebase(email: String): Boolean {
        var returnValue = false

        for (user in _usersList.value) {
            if (email == user.userMail) {
                returnValue = true
            }
        }
        return returnValue
    }

    // Function to reset password
    @SuppressLint("SuspiciousIndentation")
    fun resetPassword(email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        _resetPasswordState.value = ResetPasswordState.Loading

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _resetPasswordState.value = ResetPasswordState.Success
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isResetPw = true,
                            isResetPwSuccessful = true
                        )

                    } else {
                        _resetPasswordState.value = ResetPasswordState.Error(
                            task.exception?.message ?: "Failed to send reset email"
                        )
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isResetPw = true,
                            isResetPwSuccessful = false
                        )
                    }
                }
    }

    // State class for password reset
    sealed class ResetPasswordState {
        object Initial : ResetPasswordState()
        object Loading : ResetPasswordState()
        object Success : ResetPasswordState()
        data class Error(val message: String) : ResetPasswordState()
    }

    // ViewModel Factory
    class LoginRegisterViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginRegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginRegisterViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}