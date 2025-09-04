package com.example.ass1.ui.screen.report

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ReportHistoryScreen(
    reportViewModel: ReportViewModel
) {

    val historyRecord = reportViewModel.userReportList
    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    if (reportUiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LaunchedEffect(
        reportUiState.isEdit,
        reportUiState.isLoading,
        reportUiState.isEditSuccessful
    ) {
        // Only proceed if edit booking was made and loading has finished
        if (reportUiState.isEdit && !reportUiState.isLoading) {
            if (reportUiState.isEditSuccessful) {
                snackbarHostState.showSnackbar(
                    message = "Update report successfully.",
                    duration = SnackbarDuration.Short
                )
                delay(200)
                reportViewModel.resetReportForm()
            } else {
                snackbarHostState.showSnackbar(
                    message = "Report is fail to update. Please try again.",
                    duration = SnackbarDuration.Short
                )
                reportViewModel.resetReportForm()
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Color(0xFF4A7ABF))
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp, horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Report History Card
                historyRecord.value.forEach { history ->
                    ReportHistoryCard(
                        history = history,
                        onClick = { reportViewModel.getClickReport(history) },
                        reportViewModel = reportViewModel
                    )
                }

                //Show dialog when report is selected
                if (reportUiState.clickReport != null) {
                    ReportDialog(
                        rpHistory = reportUiState.clickReport!!,
                        onDismiss = { reportViewModel.getClickReport(null) },
                        onDelete = {
                            reportViewModel.getSelectedReport(reportUiState.clickReport!!)
                            reportViewModel.updateSelectedReportStatus(3)
                            reportViewModel.updateReportToFirebase()
                        },
                        reportViewModel = reportViewModel
                    )
                }

            }
        }
    }
}