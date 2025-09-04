package com.example.ass1.ui.screen.report

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminReportScreen(
    reportViewModel: ReportViewModel
) {
    
    val reportUiState by reportViewModel.uiState.collectAsStateWithLifecycle()
    val historyRecord = reportViewModel.reportList.value

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
            LazyColumn(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 20.dp, horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Report History Card
                items(historyRecord) { history ->
                    ReportHistoryCard(
                        history = history,
                        onClick = { reportViewModel.getClickReport(history) },
                        reportViewModel = reportViewModel
                    )
                }
            }
        }
        //Show dialog when report is selected
        if (reportUiState.clickReport != null) {
            UpdateReportDialog(
                rpHistory = reportUiState.clickReport!!,
                onUpdate = {
                    reportViewModel.getSelectedReport(reportUiState.clickReport!!)
                    reportViewModel.updateReportToFirebase()
                    reportViewModel.getClickReport(null)
                },
                reportViewModel = reportViewModel,
                onDismiss = { reportViewModel.getClickReport(null) }
            )
        }
    }
}