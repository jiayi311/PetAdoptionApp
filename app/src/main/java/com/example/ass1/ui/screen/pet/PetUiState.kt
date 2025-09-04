package com.example.ass1.ui.screen.pet

data class PetUiState(
    val age: Int = 0,
    val breed: String = "",
    val description: String = "",
    val deepDescription: String = "",
    val image1: String = "",
    val image2: String = "",
    val petId: String = "",
    val petName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedPet: String = "",
    val searchQuery: String = "",
    val selectedPetIndex: Int = 0,
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val editAge: String = "",

    val newPetName: String = "",
    val newPetBreed: String = "",
    val newPetAge: String = "",
    val newPetDescription: String = "",
    val newPetShortDescription: String = "",
    val newPetImage1: String = "",
    val newPetImage2: String = "",
    val isAddPetSuccess: Boolean = false,
)

