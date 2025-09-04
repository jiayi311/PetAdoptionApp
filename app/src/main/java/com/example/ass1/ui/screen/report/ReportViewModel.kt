package com.example.ass1.ui.screen.report

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ass1.model.Report
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.screen.profile.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale

class ReportViewModel(
    private val profileViewModel: ProfileViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    //forFirebase
    private val db = Firebase.firestore
    private val reportsCollection = db.collection("Report")

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _submitResult = MutableStateFlow<Boolean?>(null)
    val submitResult: StateFlow<Boolean?> = _submitResult.asStateFlow()

    private val _reportCount = MutableStateFlow(0)

    private val _reportList = MutableStateFlow<List<Report>>(emptyList())
    val reportList: StateFlow<List<Report>> = _reportList.asStateFlow()
    
    // Add a new StateFlow for user-specific reports
    private val _userReportList = MutableStateFlow<List<Report>>(emptyList())
    val userReportList: StateFlow<List<Report>> = _userReportList.asStateFlow()

    init {
        listenForReports()
        // Update user reports whenever the report list changes
        viewModelScope.launch {
            profileViewModel.currentUser.collect { user ->
                filterUserReports(user.userId)
            }
        }
    }

    fun updateReportToFirebase() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val updatedRp = _uiState.value.selectedReport


            try{
                // Query to find the document with this report id
                val querySnapshot = db.collection("Report")
                    .whereEqualTo("reportCaseId", updatedRp.reportCaseId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Report").document(documentId)
                        .set(updatedRp)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Report not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update report: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

    fun updateSelectedReportStatus(status: Int) {
        _uiState.value = _uiState.value.copy(
            selectedReport = _uiState.value.selectedReport.copy(status = status))
    }

    fun updateClickReportStatus(status: Int) {
        if (_uiState.value.clickReport == null) return

        _uiState.value = _uiState.value.copy(
            clickReport = _uiState.value.clickReport!!.copy(status = status))
    }

    fun getSelectedReport(rp: Report) {
        _uiState.value = _uiState.value.copy(selectedReport = rp)
    }

    fun getClickReport(rp: Report?) {
        _uiState.value = _uiState.value.copy(clickReport = rp)
    }

    fun getStatusColor(status: Int): Color {
        return when (status) {
            0 -> Color(0xffd5dfeb)
            1 -> Color(0xfffdff8a)
            2 -> Color(0xffbaffa2)
            3 -> Color(0xFFdc453a)
            else -> Color(0xffd5dfeb)
        }
    }

    fun getStatus(status: Int): String {
        return when (status) {
            0 -> "Pending"
            1 -> "Processing"
            2 -> "Completed"
            3 -> "Canceled"
            else -> "Pending"
        }
    }

    fun getFormattedDateFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun updateLocation(newLocation: String) {
        _uiState.update { currentState ->
            currentState.copy(reportLocation = newLocation)
        }
    }

    fun updateTabState(newTab: Int) {
        _uiState.update { currentState ->
            currentState.copy(tabState = newTab)
        }
    }

    fun updateSelectedStatus(status: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedStatus = status)
        }
    }

    fun updateDetails(newDetails: String) {
        _uiState.update { currentState ->
            currentState.copy(reportDetails = newDetails)
        }
    }

    fun resetReportForm() {
        _uiState.update { currentState ->
            currentState.copy(
                reportDetails = "",
                reportLocation = "",
                uploadedImageString = "",
                uploadedImageName = "",
                isEdit = false,
                isEditSuccessful = false,
                isLoading = false,
                errorMessage = null,
                selectedStatus = 0,
                selectedReport = Report(),
                clickReport = null
            )
        }
        _errorMessage.value = null
    }

    fun resetSubmitResult() {
        _submitResult.value = null
    }

    fun submitNewReport() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null
                _submitResult.value = null

                // Get current user from ProfileViewModel
                val currentUser = profileViewModel.currentUser.value
                val currentUserId = currentUser.userId

                val reportObj = Report(
                    adminComment = "",
                    details = _uiState.value.reportDetails,
                    location = _uiState.value.reportLocation,
                    reportCaseId = autoGenerateReportId(),
                    reportTime = Timestamp.now(),
                    reporterId = currentUserId,
                    status = 0,
                    reportImage = _uiState.value.uploadedImageString,
                    reportImageName = _uiState.value.uploadedImageName
                )

                reportsCollection.add(reportObj).await()

                resetReportForm()
                _submitResult.value = true
            } catch (e: Exception) {
                _errorMessage.value = "Failed to submit report: ${e.message}"
                _submitResult.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun autoGenerateReportId(): String {
        val currentReportCount = (_reportCount.value + 1)
        _reportCount.value = currentReportCount
        val newReportId = String.format(Locale.ROOT, "RP%03d", currentReportCount)
        return newReportId
    }

    private fun listenForReports() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Report")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _errorMessage.value = "Failed to listen for reports: ${error.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val reportsList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Report::class.java)
                    }
                    val sortedList = reportsList.sortedByDescending { it.reportCaseId }
                    _reportList.value = sortedList
                    _reportCount.value = _reportList.value.size
                    
                    // Update user reports whenever the report list changes
                    filterUserReports(profileViewModel.currentUser.value.userId)
                }
            }
    }

    private fun filterUserReports(userId: String) {
        // Add null check for userId
        if (userId.isBlank()) {
            return
        }

        // Create a new list with the filtered donations
        val filteredList = _reportList.value.filter { report ->
            // Add null check for donation.donatorId
            val reporterId = report.reporterId ?: ""
            val matches = reporterId == userId
            matches
        }
        _userReportList.value = filteredList
    }

    fun addImages(newImage: UploadedImage) {
        _uiState.update { currentState ->
            currentState.copy(
                uploadedImageString = newImage.base64Data,
                uploadedImageName = newImage.name)
        }
    }

    fun removeImage() {
        _uiState.update { currentState ->
            currentState.copy(
                uploadedImageString = "",
                uploadedImageName = "")
        }
    }

    // Add ViewModel Factory
    class ReportViewModelFactory(
        private val profileViewModel: ProfileViewModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReportViewModel(profileViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
