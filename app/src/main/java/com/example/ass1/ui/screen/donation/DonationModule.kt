package com.example.ass1.ui.screen.donation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ass1.R
import com.example.ass1.model.TabItem
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.component.TabBar

enum class DonationTab(@StringRes val title: Int) {
    Donation(title = R.string.donation),
    History(title = R.string.history),
    Expenses(title = R.string.expenses)
}

@Composable
fun DonationModule(
    content: @Composable () -> Unit,
    donationViewModel: DonationViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDonate: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToExpenses: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    onNavigateToAboutUs: () -> Unit
) {
    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    var selectedNavItem by remember { mutableStateOf(0) }
    val donationUiState by donationViewModel.uiState.collectAsStateWithLifecycle()

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    val tabs = listOf(
        TabItem(
            title = "Donation",
            route = DonationTab.Donation.name,
            command = {
                onNavigateToDonate()
                donationViewModel.updateTabState(0)
            }
        ),
        TabItem(
            title = "History",
            route = DonationTab.History.name,
            command = {
                onNavigateToHistory()
                donationViewModel.updateTabState(1)
            }
        ),
        TabItem(
            title = "Expenses",
            route = DonationTab.Expenses.name,
            command = {
                onNavigateToExpenses()
                donationViewModel.updateTabState(2)
            }
        )
    )

    if (isTablet) {
        if (isLandscape) {
            Row {
                SideBar(
                    currentScreen = "Donation",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = {},
                    onBackHome = onNavigateToHome,
                    onProfileClick = onNavigateToProfile,
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Donation",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        TabBar(
                            tabs = tabs,
                            selectedTab = donationUiState.tabState,
                            onTabSelected = { donationViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                        content()
                    }
                }
            }
        } else {
            Row {
                SideBar(
                    currentScreen = "Donation",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = {},
                    onBackHome = onNavigateToHome,
                    onProfileClick = onNavigateToProfile,
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Donation",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        TabBar(
                            tabs = tabs,
                            selectedTab = donationUiState.tabState,
                            onTabSelected = { donationViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                        content()
                    }
                }
            }
        }
    } else {
        if (isLandscape) {
            Row {
                SideBar(
                    currentScreen = "Donation",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = {},
                    onBackHome = onNavigateToHome,
                    onProfileClick = onNavigateToProfile,
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Donation",
                            modifier = Modifier,
                            onNavigateBack = { onNavigateBack() },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                    }
                ) { paddingValues ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        TabBar(
                            tabs = tabs,
                            selectedTab = donationUiState.tabState,
                            onTabSelected = { donationViewModel.updateTabState(it) },
                            isTablet = isTablet,
                            isLandscape = isLandscape,
                        )
                        content()
                    }
                }
            }
        } else {
            Scaffold(
                topBar = {
                    MyTopBar(
                        "Donation",
                        modifier = Modifier,
                        onNavigateBack = { onNavigateBack() },
                        isTablet = isTablet,
                        isLandscape = isLandscape,
                    )
                },
                bottomBar = {
                    MyBottomBar(
                        selectedItem = selectedNavItem,
                        onClickHome = { onNavigateToHome() },
                        onClickCommunity = { onNavigateToCommunity() },
                        onClickProfile = { onNavigateToProfile() }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    TabBar(
                        tabs = tabs,
                        selectedTab = donationUiState.tabState,
                        onTabSelected = { donationViewModel.updateTabState(it) },
                        isTablet = isTablet,
                        isLandscape = isLandscape,
                    )
                    content()
                }
            }
        }

    }
}