package com.example.ass1.ui.screen.profile

import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.ass1.ui.component.MyBottomBar
import com.example.ass1.ui.component.MyTopBar
import com.example.ass1.ui.component.SetSystemNavColor
import com.example.ass1.ui.component.SideBar
import com.example.ass1.ui.component.TabBar
import com.example.ass1.ui.screen.donation.DonationViewModel

@Composable
fun ProfileModule(
    profileViewModel: ProfileViewModel,
    content: @Composable () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToHome : () -> Unit,
    onNavigateToCommunity : () -> Unit,
    onNavigateToPetList: () -> Unit = {},
    onNavigateToBookingList: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
    onNavigateToDonation: () -> Unit = {},
    onNavigateToProfile: () -> Unit,
    onNavigateToAboutUs: () -> Unit
) {

    // Get current configuration
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    // Determine if it's a tablet (using a common threshold of 600dp)
    val isTablet = screenWidth > 600.dp && screenHeight > 600.dp

    // Determine if it's in landscape mode
    val isLandscape = screenWidth > screenHeight

    var selectedNavItem by remember { mutableStateOf(3) }
    SetSystemNavColor(0xFF4A7ABF, 0xFF4A7ABF)

    if (isTablet) {
        if (isLandscape) {
            Row{
                SideBar(
                    currentScreen = "Profile",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = onNavigateToDonation,
                    onBackHome = onNavigateToHome,
                    onProfileClick = { },
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Profile",
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
                        content()
                    }
                }
            }
        } else {
            Row{
                SideBar(
                    currentScreen = "Profile",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = onNavigateToDonation,
                    onBackHome = onNavigateToHome,
                    onProfileClick = { },
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Profile",
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
                        content()
                    }
                }
            }
        }

    } else {
        if (isLandscape) {
            Row{
                SideBar(
                    currentScreen = "Profile",
                    onPetListClick = onNavigateToPetList,
                    onCommunityClick = onNavigateToCommunity,
                    onBookingListClick = onNavigateToBookingList,
                    onReportClick = onNavigateToReport,
                    onDonationClick = onNavigateToDonation,
                    onBackHome = onNavigateToHome,
                    onProfileClick = { },
                    onAboutUsClick = onNavigateToAboutUs,
                    isTablet = isTablet,
                    isLandscape = isLandscape
                )

                Scaffold(
                    topBar = {
                        MyTopBar(
                            "Profile",
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
                        content()
                    }
                }
            }
        } else {
            Scaffold(
                topBar = {
                    MyTopBar(
                        "Profile",
                        modifier = Modifier,
                        onNavigateBack = { onNavigateBack() },
                        isTablet = isTablet,
                        isLandscape = isLandscape,
                    )
                },
                bottomBar = {
                    MyBottomBar(
                        selectedItem = selectedNavItem,
                        onClickHome = {onNavigateToHome()},
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
                    content()
                }
            }
        }
    }

}