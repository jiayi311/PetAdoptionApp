package com.example.ass1.ui.screen.pet

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ass1.model.Pet
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


class PetViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PetUiState())
    val uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val petsCollection = db.collection("Pet")

    private val _petCount = MutableStateFlow(0)
    private val _petsList = MutableStateFlow<List<Pet>>(emptyList())
    val petsList: MutableStateFlow<List<Pet>> = _petsList
    private val _petsListShow = MutableStateFlow<List<Pet>>(emptyList())
    val petsListShow: StateFlow<List<Pet>> = _petsListShow

    private var _currentPet = MutableStateFlow(Pet())
    var currentPet: StateFlow<Pet> = _currentPet.asStateFlow()

    init {
        listenForPets()
    }

    fun updateNewPetName(name: String) {
        _uiState.update { it.copy(newPetName = name) }
    }

    fun updateNewPetBreed(breed: String) {
        _uiState.update { it.copy(newPetBreed = breed) }
    }

    fun updateNewPetAge(age: String) {
        _uiState.update { it.copy(newPetAge = age) }
    }

    fun updateNewPetDescription(description: String) {
        _uiState.update { it.copy(newPetDescription = description) }
    }

    fun updateNewPetShortDescription(shortDescription: String) {
        _uiState.update { it.copy(newPetShortDescription = shortDescription) }
    }

    fun updateNewPetImage1(imageBase64: String) {
        _uiState.update { it.copy(newPetImage1 = imageBase64) }
    }

    fun updateNewPetImage2(imageBase64: String) {
        _uiState.update { it.copy(newPetImage2 = imageBase64) }
    }

    fun validateAddPetForm(): Boolean {
        // Reset error message
        _uiState.update { it.copy(errorMessage = null) }

        val name = _uiState.value.newPetName
        val breed = _uiState.value.newPetBreed
        val age = _uiState.value.newPetAge
        val description = _uiState.value.newPetDescription
        val shortDescription = _uiState.value.newPetShortDescription
        val image1 = _uiState.value.newPetImage1

        // Check required fields
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Pet name is required") }
            return false
        }

        if (breed.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Breed is required") }
            return false
        }

        if (age.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Age is required") }
            return false
        }

        try {
            val ageInt = age.toInt()
            if (ageInt < 0) {
                _uiState.update { it.copy(errorMessage = "Age cannot be negative") }
                return false
            }
        } catch (e: NumberFormatException) {
            _uiState.update { it.copy(errorMessage = "Age must be a valid number") }
            return false
        }

        if (description.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Description is required") }
            return false
        }

        if (shortDescription.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Short description is required") }
            return false
        }

        if (image1.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Primary image is required") }
            return false
        }

        // Check if name contains numbers
        if (name.any { it.isDigit() }) {
            _uiState.update { it.copy(errorMessage = "Pet name should not contain numbers") }
            return false
        }

        // Check if breed contains numbers
        if (breed.any { it.isDigit() }) {
            _uiState.update { it.copy(errorMessage = "Breed should not contain numbers") }
            return false
        }

        return true
    }

    fun addNewPet() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Get the highest pet ID from the current list
                val highestPetId = _petsList.value
                    .mapNotNull { it.petId.toIntOrNull() }
                    .maxOrNull() ?: 0

                // Create a new pet with the highest ID + 1
                val newPetId = (highestPetId + 1).toString()

                val newPet = Pet(
                    petId = newPetId,
                    petName = _uiState.value.newPetName,
                    breed = _uiState.value.newPetBreed,
                    age = _uiState.value.newPetAge.toInt(),
                    description = _uiState.value.newPetShortDescription,
                    deepDescription = _uiState.value.newPetDescription,
                    image1 = _uiState.value.newPetImage1,
                    image2 = _uiState.value.newPetImage2,
                )

                // Add the new pet to Firestore
                db.collection("Pet")
                    .add(newPet)
                    .await()

                _uiState.update { it.copy(isAddPetSuccess = true) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = "Failed to add pet: ${e.message}",
                        isAddPetSuccess = false
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun validateEditForm(): Boolean {
        var returnValue: Boolean = true

        val age = _uiState.value.age
        val breed = _uiState.value.breed
        val name = _uiState.value.petName
        val des = _uiState.value.deepDescription

        if(
            (age == _currentPet.value.age) &&
            (breed == _currentPet.value.breed) &&
            (name == _currentPet.value.petName) &&
            (des == _currentPet.value.deepDescription)
        ) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Cannot save without new information"
            )
            returnValue = false
        }

        if(age != _currentPet.value.age) {
            if(age < 0) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Age should not be negative."
                )
                returnValue = false
            }

        }

        if(breed != _currentPet.value.breed) {
            if(breed.any { it.isDigit() }) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Breed should not have any number."
                )
                returnValue = false
            }
        }

        if(des != _currentPet.value.deepDescription) {
            if(breed.any { it.isDigit() }) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Breed should not have any number."
                )
                returnValue = false
            }
        }

        if(name != _currentPet.value.petName) {
            if(name.any { it.isDigit() }) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Name should not have any number."
                )
                returnValue = false
            }
        }

        return returnValue
    }

    fun updateEditData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val updatedPet = Pet()
            updatedPet.petId = _currentPet.value.petId
            updatedPet.image1 = _currentPet.value.image1
            updatedPet.image2 = _currentPet.value.image2
            updatedPet.description = _currentPet.value.description

            if(_uiState.value.petName != _currentPet.value.petName && _uiState.value.petName.isNotBlank()) {
                updatedPet.petName = _uiState.value.petName
            }else {
                updatedPet.petName = _currentPet.value.petName
            }

            if(_uiState.value.breed != _currentPet.value.breed && _uiState.value.breed.isNotBlank()) {
                updatedPet.breed = _uiState.value.breed
            }else {
                updatedPet.breed = _currentPet.value.breed
            }

            if(_uiState.value.editAge.toInt() != _currentPet.value.age) {
                updatedPet.age = _uiState.value.editAge.toInt()
            }else {
                updatedPet.age = _currentPet.value.age
            }

            if(_uiState.value.deepDescription != _currentPet.value.deepDescription) {
                updatedPet.deepDescription = _uiState.value.deepDescription
            }else {
                updatedPet.deepDescription = _currentPet.value.deepDescription
            }


            try{
                // Query to find the document with this petId
                val querySnapshot = db.collection("Pet")
                    .whereEqualTo("petId", updatedPet.petId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Pet").document(documentId)
                        .set(updatedPet)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Pet not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update pet: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

    fun resetEditForm () {
        _uiState.value = _uiState.value.copy(
            age = 0,
            breed = "",
            description = "",
            deepDescription = "",
            image1 = "",
            image2 = "",
            petId = "",
            petName  = "",
            isLoading  = false,
            errorMessage  = null,
            searchQuery  = "",
            selectedPetIndex = 0,
            isEdit = false,
            isEditSuccessful = false,
            newPetName = "",
            newPetBreed = "",
            newPetAge = "",
            newPetDescription = "",
            newPetShortDescription = "",
            newPetImage1 = "",
            newPetImage2 = "",
            isAddPetSuccess = false,
        )
    }

    @SuppressLint("SuspiciousIndentation")
    fun assignPetInfoToCurrentPet() {
        val pet = petsList.value.find { it.petId == uiState.value.selectedPet }
        if(pet == null) return
            _currentPet.value = _currentPet.value.copy(
                age = pet.age,
                breed = pet.breed,
                description = pet.description,
                deepDescription = pet.deepDescription,
                petId = pet.petId,
                petName = pet.petName,
                image1 = pet.image1,
                image2 = pet.image2
            )
            _uiState.value = _uiState.value.copy(
                age = pet.age,
            )
        }

    private fun filterPets() {
        if (_uiState.value.searchQuery.isBlank()) {
            _petsListShow.value = _petsList.value
            return
        }

        _petsListShow.value = _petsList.value.filter {
            it.petName.contains(_uiState.value.searchQuery, ignoreCase = true)
        }
    }

    private fun listenForPets() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Pet")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for pets: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val petsList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Pet::class.java)
                    }
                    val sortedList = petsList.sortedByDescending { it.petId }
                    _petsList.value = sortedList
                    _petsListShow.value = sortedList  // Initialize the filtered list with all pets
                    _petCount.value = _petsList.value.size
                }
            }
    }

    fun updatePetAge(age: String) {
        _uiState.value = _uiState.value.copy(
            editAge = age
        )
    }

    fun updatePetBreed(breed: String) {
        _uiState.value = _uiState.value.copy(
            breed = breed
        )
    }

    fun updatePetName(name: String) {
        _uiState.value = _uiState.value.copy(
            petName = name
        )
    }

    fun updatePetDeepDes(des: String) {
        _uiState.value = _uiState.value.copy(
            deepDescription = des
        )
    }

    fun updateSelectedPet(petId: String) {
        _uiState.value = _uiState.value.copy(
            selectedPet = petId
        )
    }

    fun updateSearchQuery(searchBar: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = searchBar
        )
        filterPets()
    }

    fun getCurrentPetIndex(petId: String) {

        val foundPet = _petsList.value.find { it.petId == petId }

        if (foundPet != null) {
            _uiState.value = _uiState.value.copy(
                selectedPetIndex = _petsList.value.indexOfFirst { it.petId == petId }
            )
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Cannot find pet!"
            )
        }
    }

    fun navigateToPreviousPet() {
        if (_uiState.value.selectedPetIndex > 0) {
            val previousIndex = (_uiState.value.selectedPetIndex - 1)
            val previousPetId = _petsList.value[previousIndex].petId
            _uiState.value = _uiState.value.copy(
                selectedPetIndex = previousIndex,
                selectedPet = previousPetId
            )
        } else {
            val previousIndex = (_petCount.value - 1)
            val previousPetId = _petsList.value[previousIndex].petId
            _uiState.value = _uiState.value.copy(
                selectedPetIndex = previousIndex,
                selectedPet = previousPetId
            )
        }
    }

    fun navigateToNextPet() {
        if (_uiState.value.selectedPetIndex < _petsList.value.size - 1) {
            val nextIndex = (_uiState.value.selectedPetIndex + 1)
            val nextPetId = _petsList.value[nextIndex].petId
            _uiState.value = _uiState.value.copy(
                selectedPetIndex = nextIndex,
                selectedPet = nextPetId
            )
        } else {
            // Loop around to the first pet
            val firstPetId = _petsList.value[0].petId
            _uiState.value = _uiState.value.copy(
                selectedPetIndex = 0,
                selectedPet = firstPetId
            )
        }
    }
}