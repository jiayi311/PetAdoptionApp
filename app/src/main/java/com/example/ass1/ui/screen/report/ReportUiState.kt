package com.example.ass1.ui.screen.report

import com.example.ass1.model.Report
import com.example.ass1.model.UploadedImage

data class ReportUiState(
    val reportLocation: String = "",
    val reportDetails: String = "",
    val uploadedImageName: String = "",
    val uploadedImageString: String = "",
    val tabState: Int = 0,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val isEdit: Boolean = false,
    val isEditSuccessful: Boolean = false,
    val selectedReport: Report = Report(),
    val clickReport: Report? = null,
    val selectedStatus: Int = 0
)
