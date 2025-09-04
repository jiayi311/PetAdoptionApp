package com.example.ass1.ui.screen.profile

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ass1.data.repository.UserRepository
import com.example.ass1.model.User
import com.example.ass1.ui.screen.loginRegister.LoginRegisterViewModel.ResetPasswordState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository): ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth =  FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val usersCollection = db.collection("User")

    private var _currentUser = MutableStateFlow(User())
    var currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Initial)

    init{
        loadUserProfile()
    }

    fun resetProfileUiState() {
        _uiState.value = _uiState.value.copy(
            userId = "",
            userName = "",
            userMail = "",
            userPhone = "",
//            userGender = 0,
            errorMessage = null,
            isLoading = false,
            isEdit = false,
            isEditSuccessful = false,
            isChangePw = false,
            isChangePwSuccessful = false,
        )
    }

    fun assignUserDataToUiState() {
        _uiState.value = _uiState.value.copy(
            userMail = _currentUser.value.userMail,
            userGender = _currentUser.value.userGender,
            userPhone = _currentUser.value.userPhone,
            userName = _currentUser.value.userName,
            userId = _currentUser.value.userId
        )
    }

    fun updateUserPhone(phone: String) {
        // Apply formatting and validate max length
        val formattedPhone = formatPhoneNumber(phone)
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

    fun updateUserGender(gender: Int) {
        _uiState.value = _uiState.value.copy(
            userGender = gender
        )
    }

    fun updateUserName(name: String) {
        _uiState.value = _uiState.value.copy(
            userName = name
        )
    }

    fun getGender(gender: Int): String {
        if(gender == 0) {
            return "Unknown"
        } else if (gender == 1) {
            return "Male"
        }else if (gender == 2){
            return "Female"
        } else{
            return "Alien"
        }
    }

    fun validateEditForm(): Boolean {
        var returnValue: Boolean = true
        val phoneNumber = _uiState.value.userPhone
        val fullName = _uiState.value.userName
        val gender = _uiState.value.userGender

        if(fullName.isBlank() && phoneNumber.isBlank() && phoneNumber.isBlank() &&
            (gender == _currentUser.value.userGender)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Cannot save without new information"
            )
            returnValue = false

        }

        if(phoneNumber.isNotBlank()) {
            if(!phoneValidation(phoneNumber)) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = _uiState.value.phoneErrorMessage
                )
                returnValue = false
            }
        }

        if(fullName.isNotBlank()) {
            if(fullName.any { it.isDigit() }) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Name should not contains any number! "
                )
                returnValue = false
            }
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

    fun loadUserProfile() {
        val curUser = auth.currentUser ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            userRepository.getUserByEmailStream(curUser.email ?: "")
                .collectLatest { user ->
                    if (user != null) {
                        _currentUser.value  = User(
                            userId = user.userId,
                            userName = user.userName,
                            userMail = user.userMail,
                            userGender = user.userGender,
                            userPhone = user.userPhone,
                        )
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                userGender = user.userGender
                            )
                        }
                    } else {
                        // If not in local database, try to fetch from Firebase
                        userRepository.fetchUserByEmail(curUser.email ?: "")
                            .onSuccess { fetchedUser ->
                                _currentUser.value = User(
                                    userId = fetchedUser.userId,
                                    userName = fetchedUser.userName,
                                    userMail = fetchedUser.userMail,
                                    userGender = fetchedUser.userGender,
                                    userPhone = fetchedUser.userPhone,
                                )
                                currentUser = _currentUser
                                _uiState.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                            }
                            .onFailure { exception ->
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = "Failed to load user data!"
                                    )
                                }
                            }
                    }
                }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val updatedUser = User()
            updatedUser.userId = _currentUser.value.userId
            updatedUser.userMail = _currentUser.value.userMail
            if(_uiState.value.userName != _currentUser.value.userName && _uiState.value.userName.isNotBlank()) {
                updatedUser.userName = _uiState.value.userName
            }else {
                updatedUser.userName = _currentUser.value.userName
            }

            if(_uiState.value.userPhone != _currentUser.value.userPhone && _uiState.value.userPhone.isNotBlank()) {
                updatedUser.userPhone = _uiState.value.userPhone
            }else {
                updatedUser.userPhone = _currentUser.value.userPhone
            }

            if(_uiState.value.userGender != _currentUser.value.userGender) {
                updatedUser.userGender = _uiState.value.userGender
            }else {
                updatedUser.userGender = _currentUser.value.userGender
            }

            Log.d("UpdateDate",updatedUser.userId)
            Log.d("UpdateDate",updatedUser.userName)
            Log.d("UpdateDate",updatedUser.userMail)
            Log.d("UpdateDate",updatedUser.userGender.toString())
            Log.d("UpdateDate",updatedUser.userPhone)

            // Update in Firebase and Room
            userRepository.updateUserInFirebase(updatedUser)
                .onSuccess {
                    _currentUser.value = User(
                        userId = updatedUser.userId,
                        userName = updatedUser.userName,
                        userMail = updatedUser.userMail,
                        userGender = updatedUser.userGender,
                        userPhone = updatedUser.userPhone,
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEdit = true,
                            isEditSuccessful = true
                        )
                    }
                }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            errorMessage = "Failed to update profile: ${exception.message}",
                            isLoading = false,
                            isEdit = true
                        )
                    }
                }
        }
    }

    // Function to reset password
    @SuppressLint("SuspiciousIndentation")
    fun changePassword(email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        _resetPasswordState.value = ResetPasswordState.Loading

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _resetPasswordState.value = ResetPasswordState.Success
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isChangePw = true,
                        isChangePwSuccessful = true
                    )

                } else {
                    _resetPasswordState.value = ResetPasswordState.Error(
                        task.exception?.message ?: "Failed to send reset email"
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isChangePw = true,
                        isChangePwSuccessful = false
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
    class ProfileViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(userRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
