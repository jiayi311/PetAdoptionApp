package com.example.ass1.ui.screen.aboutUs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ass1.model.AboutUs
import com.example.ass1.model.Pet
import com.example.ass1.ui.screen.pet.PetUiState
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

class AboutUsViewModel(): ViewModel() {

    private val _uiState = MutableStateFlow(AboutUsUiState())
    val uiState: StateFlow<AboutUsUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val aboutUsCollection = db.collection("aboutUs")

    private val _aboutUsData = MutableStateFlow<AboutUs>(AboutUs())
    val aboutUsData: MutableStateFlow<AboutUs> = _aboutUsData

    init {
        listenForAboutUs()
    }

    fun assignDataToUiState() {
        _uiState.value = _uiState.value.copy(
            mission = _aboutUsData.value.mission,
            operationHour = _aboutUsData.value.operationHour,
            address = _aboutUsData.value.address
        )
    }

    private fun listenForAboutUs() {
        val db = FirebaseFirestore.getInstance()
        db.collection("aboutUs")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for aboutUs: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val aboutUs = snapshot.documents.mapNotNull { document ->
                        document.toObject(AboutUs::class.java)
                    }
                    _aboutUsData.value = aboutUs[0]
            }
        }
    }

    fun updateShowDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(
            showContactDialog = show
        )
    }

    fun updateMission(mission: String) {
        _uiState.value = _uiState.value.copy(
            mission = mission
        )
    }

    fun updateOperationHour(operationHour: String) {
        _uiState.value = _uiState.value.copy(
           operationHour = operationHour
        )
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(
            address = address
        )
    }

    fun resetEditForm() {
        _uiState.value = _uiState.value.copy(
            showContactDialog = false,
            errorMessage = null,
            isEdit = false,
            isLoading = false,
            isEditSuccessful = false,
            mission = "",
            operationHour = "",
            phoneNumber = "",
            email = "",
            address = "",
        )
    }

    fun validateEditForm(): Boolean {
        var returnValue: Boolean = true

        val mission = _uiState.value.mission
        val operationHour = _uiState.value.operationHour
        val address = _uiState.value.address

        if(
            (mission == _aboutUsData.value.mission) &&
            (operationHour == _aboutUsData.value.operationHour) &&
            (address == _aboutUsData.value.address)
        ) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Cannot save without new information"
            )
            returnValue = false
        }

        return returnValue
    }

    fun updateEditData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            var updatedData = AboutUs()
            updatedData = updatedData.copy(
                phoneNumber = _aboutUsData.value.phoneNumber,
                email = _aboutUsData.value.email
            )
            if(_uiState.value.mission != _aboutUsData.value.mission && _uiState.value.mission.isNotBlank()) {
                updatedData.mission = _uiState.value.mission
            }else {
                updatedData.mission = _aboutUsData.value.mission
            }

            if(_uiState.value.operationHour !=  _aboutUsData.value.operationHour && _uiState.value.operationHour.isNotBlank()) {
                updatedData.operationHour = _uiState.value.operationHour
            }else {
                updatedData.operationHour =  _aboutUsData.value.operationHour
            }

            if(_uiState.value.address !=  _aboutUsData.value.address && _uiState.value.address.isNotBlank()) {
                updatedData.address = _uiState.value.address
            }else {
                updatedData.address = _aboutUsData.value.address
            }

            try{
                // Query to find the document with this petId
                val querySnapshot = db.collection("aboutUs")
                    .document("aboutUsDocument")
                    .get()
                    .await()

                if (querySnapshot.exists()) {

                    db.collection("aboutUs").document("aboutUsDocument")
                        .set(updatedData)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "About Us data not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update about us data: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

}