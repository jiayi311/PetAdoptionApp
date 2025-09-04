package com.example.ass1.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.ass1.MainActivity
import com.example.ass1.ui.screen.aboutUs.AboutUsScreen
import com.example.ass1.ui.screen.aboutUs.AboutUsViewModel
import com.example.ass1.ui.screen.aboutUs.AdminAboutUsEditScreen
import com.example.ass1.ui.screen.aboutUs.AdminAboutUsModule
import com.example.ass1.ui.screen.aboutUs.AdminAboutUsScreen
import com.example.ass1.ui.screen.booking.AdminBookingModule
import com.example.ass1.ui.screen.booking.AdminBookingScreen
import com.example.ass1.ui.screen.booking.BookingHistoryScreen
import com.example.ass1.ui.screen.booking.BookingModule
import com.example.ass1.ui.screen.booking.BookingScreen
import com.example.ass1.ui.screen.booking.BookingViewModel
import com.example.ass1.ui.screen.community.AdminCommunityAddScreen
import com.example.ass1.ui.screen.community.AdminCommunityEditScreen
import com.example.ass1.ui.screen.community.AdminCommunityListScreen
import com.example.ass1.ui.screen.community.AdminCommunityModule
import com.example.ass1.ui.screen.community.CommunityModule
import com.example.ass1.ui.screen.community.CommunityPostScreen
import com.example.ass1.ui.screen.community.CommunityScreen
import com.example.ass1.ui.screen.community.CommunityViewModel
import com.example.ass1.ui.screen.donation.AdminDonationModule
import com.example.ass1.ui.screen.donation.AdminDonationViewScreen
import com.example.ass1.ui.screen.donation.AdminExpensesAddScreen
import com.example.ass1.ui.screen.donation.AdminExpensesEditScreen
import com.example.ass1.ui.screen.donation.AdminExpensesViewScreen
import com.example.ass1.ui.screen.donation.DonationHistoryScreen
import com.example.ass1.ui.screen.donation.DonationModule
import com.example.ass1.ui.screen.donation.DonationScreen
import com.example.ass1.ui.screen.donation.DonationViewModel
import com.example.ass1.ui.screen.donation.ExpensesScreen
import com.example.ass1.ui.screen.home.AdminHomeModule
import com.example.ass1.ui.screen.home.AdminHomeScreen
import com.example.ass1.ui.screen.home.HomeModule
import com.example.ass1.ui.screen.home.HomeScreen
import com.example.ass1.ui.screen.loginRegister.ForgotPwScreen
import com.example.ass1.ui.screen.loginRegister.LoginRegisterModule
import com.example.ass1.ui.screen.loginRegister.LoginRegisterViewModel
import com.example.ass1.ui.screen.loginRegister.LoginScreen
import com.example.ass1.ui.screen.loginRegister.RegisterScreen
import com.example.ass1.ui.screen.loginRegister.StartPageScreen
import com.example.ass1.ui.screen.pet.AdminAddPetScreen
import com.example.ass1.ui.screen.pet.AdminPetDetailsScreen
import com.example.ass1.ui.screen.pet.AdminPetListScreen
import com.example.ass1.ui.screen.pet.AdminPetModule
import com.example.ass1.ui.screen.pet.PetDetailsScreen
import com.example.ass1.ui.screen.pet.PetListScreen
import com.example.ass1.ui.screen.pet.PetModule
import com.example.ass1.ui.screen.pet.PetViewModel
import com.example.ass1.ui.screen.profile.ChangePwScreen
import com.example.ass1.ui.screen.profile.EditProfileScreen
import com.example.ass1.ui.screen.profile.ProfileModule
import com.example.ass1.ui.screen.profile.ProfileScreen
import com.example.ass1.ui.screen.profile.ProfileViewModel
import com.example.ass1.ui.screen.report.AdminReportModule
import com.example.ass1.ui.screen.report.AdminReportScreen
import com.example.ass1.ui.screen.report.ReportHistoryScreen
import com.example.ass1.ui.screen.report.ReportModule
import com.example.ass1.ui.screen.report.ReportScreen
import com.example.ass1.ui.screen.report.ReportViewModel

// Main navigation routes
sealed class AppDestinations {
    object LoginRegister : AppDestinations()
    object Home : AppDestinations()
    object AboutUs : AppDestinations()
    object Report : AppDestinations()
    object Donation : AppDestinations()
    object Community : AppDestinations()
    object Pet : AppDestinations()
    object Booking : AppDestinations()
    object Profile : AppDestinations()
    object AdminHome : AppDestinations()
    object AdminAboutUs : AppDestinations()
    object AdminReport : AppDestinations()
    object AdminDonation : AppDestinations()
    object AdminCommunity : AppDestinations()
    object AdminPet : AppDestinations()
    object AdminBooking : AppDestinations()
}

// Extension to get route names
val AppDestinations.route: String
    get() = when (this) {
        is AppDestinations.LoginRegister -> "login_register"
        is AppDestinations.Home -> "home"
        is AppDestinations.AboutUs -> "about_us"
        is AppDestinations.Report -> "report"
        is AppDestinations.Donation -> "donation"
        is AppDestinations.Community -> "community"
        is AppDestinations.Pet -> "pet"
        is AppDestinations.Booking -> "booking"
        is AppDestinations.Profile -> "profile"
        is AppDestinations.AdminHome -> "admin_home"
        is AppDestinations.AdminAboutUs -> "admin_about_us"
        is AppDestinations.AdminReport -> "admin_report"
        is AppDestinations.AdminDonation -> "admin_donation"
        is AppDestinations.AdminCommunity -> "admin_community"
        is AppDestinations.AdminPet -> "admin_pet"
        is AppDestinations.AdminBooking -> "admin_booking"
    }

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestinations.LoginRegister.route
) {

    val context = LocalContext.current
    val activity = context.findActivity() as MainActivity
    val loginRegisterViewModel: LoginRegisterViewModel = viewModel(factory = LoginRegisterViewModel.LoginRegisterViewModelFactory(activity.userRepository))
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.ProfileViewModelFactory(activity.userRepository))
    val donationViewModel: DonationViewModel = viewModel(factory = DonationViewModel.DonationViewModelFactory(profileViewModel))
    val reportViewModel: ReportViewModel = viewModel(factory = ReportViewModel.ReportViewModelFactory(profileViewModel))
    val aboutUsViewModel: AboutUsViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()
    val petViewModel: PetViewModel = viewModel()
    val bookingViewModel: BookingViewModel = viewModel(factory = BookingViewModel.BookingViewModelFactory(profileViewModel))

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Login/Register Module
        navigation(
            startDestination = LoginRegisterDestinations.Start.route,
            route = AppDestinations.LoginRegister.route
        ) {
            composable(route = LoginRegisterDestinations.Start.route) {
                LoginRegisterModule {
                    StartPageScreen(
                        onGetStartedClick = {
                            navController.navigate(LoginRegisterDestinations.Login.route)
                        }
                    )
                }
            }

            composable(route = LoginRegisterDestinations.Login.route){
                LoginRegisterModule {
                    LoginScreen(
                        loginRegisterViewModel = loginRegisterViewModel,
                        onLoginSuccess = {
                            navController.navigate(AppDestinations.Home.route) {
                                popUpTo(AppDestinations.Home.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onRegister = {
                            navController.navigate(LoginRegisterDestinations.Register.route)
                            loginRegisterViewModel.resetLoginRegisterForm()
                        },
                        onForgotPassword = {
                            navController.navigate(LoginRegisterDestinations.ForgotPassword.route)
                            loginRegisterViewModel.resetLoginRegisterForm()
                        },
                        onLoginAdmin = {
                            navController.navigate(AppDestinations.AdminHome.route) {
                                popUpTo(AppDestinations.AdminHome.route) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
            }

            composable(route = LoginRegisterDestinations.Register.route) {
                LoginRegisterModule {
                    RegisterScreen(
                        loginRegisterViewModel = loginRegisterViewModel,
                        onNavigateToLogin = {
                            navController.navigate(LoginRegisterDestinations.Login.route) {
                                popUpTo(AppDestinations.Home.route) {
                                    inclusive = true
                                }
                            }
                        },
                        onBackToLogin = { navController.popBackStack() }
                    )
                }
            }

            composable(route = LoginRegisterDestinations.ForgotPassword.route) {
                LoginRegisterModule {
                    ForgotPwScreen(
                        onBackToLogin = {
                            navController.popBackStack()
                            loginRegisterViewModel.resetLoginRegisterForm()},
                        onNavigateToLogin = {
                            loginRegisterViewModel.resetLoginRegisterForm()
                            navController.navigate(LoginRegisterDestinations.Login.route)},
                        loginRegisterViewModel = loginRegisterViewModel
                    )
                }
            }
        }

        // Home Module
        composable(route = AppDestinations.Home.route) {
            HomeModule(
                onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                content =
                {
                    HomeScreen(
                        onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                        onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                        onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                        onNavigateToPet = { navController.navigate(AppDestinations.Pet.route) },
                        onNavigateToBooking = { navController.navigate(AppDestinations.Booking.route) },
                        onNavigateToAboutUs = { navController.navigate(AppDestinations.AboutUs.route) },
                        onNavigateToCommunityPost = { navController.navigate(CommunityDestinations.PostDetails.route) },
                        onNavigateToPetDetails = { navController.navigate(PetDestinations.PetDetails.route) },
                        communityViewModel = communityViewModel,
                        petViewModel = petViewModel,
                        profileViewModel = profileViewModel
                    )
                },
                onNavigateToHome =  { navController.navigate(AppDestinations.Home.route) },
                onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
            )
        }

        // About Us Module
        composable(route = AppDestinations.AboutUs.route) {
            AboutUsScreen(
                onNavigateBack = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onClickHome = {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onClickProfile = { navController.navigate(AppDestinations.Profile.route) },
                onClickCommunity = { navController.navigate(AppDestinations.Community.route) },
                aboutUsViewModel = aboutUsViewModel,
                onNavigateToHome =  {
                    navController.navigate(AppDestinations.Home.route) {
                        popUpTo(AppDestinations.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) }
            )
        }

        // Report Module
        navigation(
            startDestination = ReportDestinations.ReportList.route,
            route = AppDestinations.Report.route
        ) {
            composable(route = ReportDestinations.ReportList.route) {
                reportViewModel.updateTabState(0)
                ReportModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHistory = { navController.navigate(ReportDestinations.ReportHistory.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    reportViewModel = reportViewModel,
                    content = {
                        ReportScreen(
                            onReportNowClick = { reportViewModel.submitNewReport() },
                            reportViewModel = reportViewModel

                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(route = ReportDestinations.ReportHistory.route) {
                reportViewModel.updateTabState(1)
                ReportModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHistory = { navController.navigate(ReportDestinations.ReportHistory.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    reportViewModel = reportViewModel,
                    content = {
                        ReportHistoryScreen(
                            reportViewModel = reportViewModel
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }
        }

        // Donation Module
        navigation(
            startDestination = DonationDestinations.DonationList.route,
            route = AppDestinations.Donation.route
        ) {
            composable(route = DonationDestinations.DonationList.route) {
                donationViewModel.updateTabState(0)
                DonationModule(
                    donationViewModel = donationViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToDonate = { navController.navigate(DonationDestinations.DonationList.route) },
                    onNavigateToHistory = { navController.navigate(DonationDestinations.DonationHistory.route) },
                    onNavigateToExpenses = { navController.navigate(DonationDestinations.DonationExpenses.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        DonationScreen(
                            donationViewModel = donationViewModel,
                            onNavigateToHistory = { navController.navigate(DonationDestinations.DonationHistory.route) }
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(route = DonationDestinations.DonationHistory.route) {
                donationViewModel.updateTabState(1)
                DonationModule(
                    donationViewModel = donationViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToDonate = { navController.navigate(DonationDestinations.DonationList.route) },
                    onNavigateToHistory = { navController.navigate(DonationDestinations.DonationHistory.route) },
                    onNavigateToExpenses = { navController.navigate(DonationDestinations.DonationExpenses.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        DonationHistoryScreen(donationViewModel = donationViewModel)
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(route = DonationDestinations.DonationExpenses.route) {
                donationViewModel.updateTabState(2)
                DonationModule(
                    donationViewModel = donationViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToDonate = { navController.navigate(DonationDestinations.DonationList.route) },
                    onNavigateToHistory = { navController.navigate(DonationDestinations.DonationHistory.route) },
                    onNavigateToExpenses = { navController.navigate(DonationDestinations.DonationExpenses.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        ExpensesScreen(donationViewModel = donationViewModel)
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }
        }

        // Profile Module
        navigation(
            startDestination = ProfileDestinations.Profile.route,
            route = AppDestinations.Profile.route
        ) {

            composable(route = ProfileDestinations.Profile.route) {
                ProfileModule(
                    profileViewModel = profileViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToProfile = {},
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    content = {
                        ProfileScreen(
                            onEditInfo = { navController.navigate(ProfileDestinations.EditProfile.route) },
                            onChangePassword = { navController.navigate(ProfileDestinations.ChangePassword.route) },
                            onSignOut = {
                                loginRegisterViewModel.signOut()
                                navController.navigate(LoginRegisterDestinations.Login.route)
                            },
                            profileViewModel = profileViewModel,
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )

            }

            composable(route = ProfileDestinations.EditProfile.route) {
                profileViewModel.resetProfileUiState()
                ProfileModule(
                    profileViewModel = profileViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(AppDestinations.Profile.route) {
                            popUpTo(AppDestinations.Profile.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    content = {
                        EditProfileScreen(
                            onNavigateToProfileScreen = {
                                navController.navigate(AppDestinations.Profile.route) {
                                    popUpTo(AppDestinations.Profile.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            profileViewModel = profileViewModel
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }


            composable(route = ProfileDestinations.ChangePassword.route) {
                ProfileModule(
                    profileViewModel = profileViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToProfile = {
                        navController.navigate(AppDestinations.Profile.route) {
                            popUpTo(AppDestinations.Profile.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    content = {
                        ChangePwScreen(
                            profileViewModel = profileViewModel,
                            onNavigateToProfile = {
                                navController.navigate(AppDestinations.Profile.route) {
                                    popUpTo(AppDestinations.Profile.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )


            }
        }

        // Community Module
        navigation(
            startDestination = CommunityDestinations.CommunityList.route,
            route = AppDestinations.Community.route
        ) {
            composable(route = CommunityDestinations.CommunityList.route) {
                CommunityModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        CommunityScreen(
                            onNavigateToPostScreen = { navController.navigate(CommunityDestinations.PostDetails.route) },
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(
                route = CommunityDestinations.PostDetails.route,
            ) {
                CommunityModule(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        CommunityPostScreen(
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }
        }

        // Pet Module
        navigation(
            startDestination = PetDestinations.PetList.route,
            route = AppDestinations.Pet.route
        ) {
            composable(route = PetDestinations.PetList.route) {
                PetModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        PetListScreen(
                            onNavigateToPetDetails = { navController.navigate(PetDestinations.PetDetails.route) },
                            petViewModel = petViewModel,
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(route = PetDestinations.PetDetails.route) {
                PetModule(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        PetDetailsScreen(
                            petViewModel = petViewModel,
                            onNavigateToBooking = { navController.navigate(AppDestinations.Booking.route) },
                        )
                    },
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }
        }

        // Booking Module
        navigation(
            startDestination = BookingDestinations.BookingList.route,
            route = AppDestinations.Booking.route
        ) {
            composable(route = BookingDestinations.BookingList.route) {
                bookingViewModel.updateTabState(0)
                BookingModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        BookingScreen(
                            onNavigateToHistory = { navController.navigate(BookingDestinations.BookingHistory.route) },
                            bookingViewModel = bookingViewModel
                        )
                    },
                    onNavigateToBooking = { navController.navigate(BookingDestinations.BookingList.route) },
                    onNavigateToHistory = { navController.navigate(BookingDestinations.BookingHistory.route) },
                    bookingViewModel = bookingViewModel,
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )
            }

            composable(route = BookingDestinations.BookingHistory.route) {
                bookingViewModel.updateTabState(1)
                BookingModule(
                    onNavigateBack = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.Community.route) },
                    onNavigateToProfile = { navController.navigate(AppDestinations.Profile.route) },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.Home.route) {
                            popUpTo(AppDestinations.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    content = {
                        BookingHistoryScreen(
                            bookingViewModel = bookingViewModel
                        )
                    },
                    onNavigateToBooking = { navController.navigate(BookingDestinations.BookingList.route) },
                    onNavigateToHistory = { navController.navigate(BookingDestinations.BookingHistory.route) },
                    bookingViewModel = bookingViewModel,
                    onNavigateToPetList = { navController.navigate(AppDestinations.Pet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.Booking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.Report.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.Donation.route) },
                    onNavigateToAboutUs =  { navController.navigate(AppDestinations.AboutUs.route) }
                )

            }
        }

        // admin part
        // Home Module
        composable(route = AppDestinations.AdminHome.route) {
            AdminHomeModule(
                content = {
                    AdminHomeScreen(
                        onSignOut = {
                            loginRegisterViewModel.signOut()
                            navController.navigate(LoginRegisterDestinations.Login.route)
                        },
                        onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                        onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                        onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                        onNavigateToPet = { navController.navigate(AppDestinations.AdminPet.route) },
                        onNavigateToBooking = { navController.navigate(AppDestinations.AdminBooking.route) },
                        onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                    )
                },
                onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                onNavigateToHome = {
                    navController.navigate(AppDestinations.AdminHome.route) {
                        popUpTo(AppDestinations.AdminHome.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                onNavigateToProfile = {},
                onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
            )
        }

        // About Us Module
        navigation(
            startDestination = AdminAboutUsDestinations.AdminAboutUsView.route,
            route = AppDestinations.AdminAboutUs.route
        ) {
            composable(route = AdminAboutUsDestinations.AdminAboutUsView.route) {
                AdminAboutUsModule(
                    content = {
                        AdminAboutUsScreen(
                            onEdit = { navController.navigate(AdminAboutUsDestinations.AdminAboutUsEdit.route) },
                            aboutUsViewModel = aboutUsViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            onNavigateToHome = { navController.navigate(AppDestinations.AdminHome.route) },
                            onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                            onNavigateToProfile = {}
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                )
            }

            composable(route = AdminAboutUsDestinations.AdminAboutUsEdit.route) {
                aboutUsViewModel.assignDataToUiState()
                AdminAboutUsModule(
                    content = {
                        AdminAboutUsEditScreen(
                            aboutUsViewModel = aboutUsViewModel,
                            onNavigateToAboutUs = { navController.navigate(AdminAboutUsDestinations.AdminAboutUsView.route) }
                        )
                    },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                )
            }
        }

        // Report Module
        composable(route = AppDestinations.AdminReport.route) {
            AdminReportModule(
                content = {
                    AdminReportScreen(
                        reportViewModel = reportViewModel
                    )
                },
                onNavigateBack = {
                    navController.navigate(AppDestinations.AdminHome.route) {
                        popUpTo(AppDestinations.AdminHome.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(AppDestinations.AdminHome.route) {
                        popUpTo(AppDestinations.AdminHome.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                reportViewModel = reportViewModel,
                onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                onNavigateToProfile = {},
                onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
            )
        }

        // Donation Module
        navigation(
            startDestination = AdminDonationDestinations.AdminDonationList.route,
            route = AppDestinations.AdminDonation.route
        ) {
            composable(route = AdminDonationDestinations.AdminDonationList.route) {
                donationViewModel.updateTabState(0)
                AdminDonationModule(
                    content = {
                        AdminDonationViewScreen(
                            donationViewModel = donationViewModel
                        )
                    },
                    donationViewModel = donationViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToDonate = {},
                    onNavigateToExpenses = { navController.navigate(AdminDonationDestinations.AdminExpensesList.route) },
                    onNavigateToHome = { navController.navigate(AppDestinations.AdminHome.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminDonationDestinations.AdminExpensesList.route) {
                donationViewModel.updateTabState(1)
                AdminDonationModule(
                    content = {
                        AdminExpensesViewScreen(
                            onNavigateToEdit = { navController.navigate(AdminDonationDestinations.AdminExpensesEdit.route) },
                            onAddNew = { navController.navigate(AdminDonationDestinations.AdminExpensesAdd.route) },
                            donationViewModel = donationViewModel
                        )
                    },
                    donationViewModel = donationViewModel,
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToDonate = { navController.navigate(AdminDonationDestinations.AdminDonationList.route) },
                    onNavigateToExpenses = { navController.navigate(AdminDonationDestinations.AdminExpensesList.route) },
                    onNavigateToHome = { navController.navigate(AppDestinations.AdminHome.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )

            }

            composable(route = AdminDonationDestinations.AdminExpensesAdd.route) {
                donationViewModel.updateTabState(1)
                AdminDonationModule(
                    content = {
                        AdminExpensesAddScreen(
                            onNavigateToExpensesList = {
                                navController.navigate(
                                    AdminDonationDestinations.AdminExpensesList.route
                                )
                            },
                            donationViewModel = donationViewModel
                        )
                    },
                    donationViewModel = donationViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDonate = { navController.navigate(AdminDonationDestinations.AdminDonationList.route) },
                    onNavigateToExpenses = { navController.navigate(AdminDonationDestinations.AdminExpensesList.route) },
                    onNavigateToHome = { navController.navigate(AppDestinations.AdminHome.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )

            }

            composable(route = AdminDonationDestinations.AdminExpensesEdit.route) {
                donationViewModel.updateTabState(1)
                donationViewModel.assignExpToUiState()
                AdminDonationModule(
                    content = {
                        AdminExpensesEditScreen(
                            onNavigateToExpensesList = {
                                navController.navigate(
                                    AdminDonationDestinations.AdminExpensesList.route
                                )
                            },
                            donationViewModel = donationViewModel
                        )
                    },
                    donationViewModel = donationViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDonate = { navController.navigate(AdminDonationDestinations.AdminDonationList.route) },
                    onNavigateToExpenses = { navController.navigate(AdminDonationDestinations.AdminExpensesList.route) },
                    onNavigateToHome = { navController.navigate(AppDestinations.AdminHome.route) },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )

            }
        }

        // Community Module
        navigation(
            startDestination = AdminCommunityDestinations.AdminCommunityList.route,
            route = AppDestinations.AdminCommunity.route
        ) {
            composable(route = AdminCommunityDestinations.AdminCommunityList.route) {
                communityViewModel.resetEditForm()
                AdminCommunityModule(
                    content = {
                        AdminCommunityListScreen(
                            onNavigateToPostScreen = {
                                navController.navigate(
                                    AdminCommunityDestinations.AdminViewPost.route
                                )
                            },
                            onEditDetails = { navController.navigate(AdminCommunityDestinations.AdminEditPost.route) },
                            onAddNewEvent = { navController.navigate(AdminCommunityDestinations.AdminAddPost.route) },
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminCommunityDestinations.AdminAddPost.route) {
                AdminCommunityModule(
                    content = {
                        AdminCommunityAddScreen(
                            onNavigateToCommunity = { navController.popBackStack() },
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminCommunityDestinations.AdminEditPost.route) {
                communityViewModel.assignCurrentEventToUiState()
                AdminCommunityModule(
                    content = {
                        AdminCommunityEditScreen(
                            onNavigateToCommunity = { navController.popBackStack() },
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminCommunityDestinations.AdminViewPost.route) {
                AdminCommunityModule(
                    content = {
                        CommunityPostScreen(
                            communityViewModel = communityViewModel
                        )
                    },
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }
        }

        // Pet Module
        navigation(
            startDestination = AdminPetDestinations.AdminPetList.route,
            route = AppDestinations.AdminPet.route
        ) {
            composable(route = AdminPetDestinations.AdminPetList.route) {
                AdminPetModule(
                    content = {
                        AdminPetListScreen(
                            onNavigateToPetDetails = { navController.navigate(AdminPetDestinations.AdminEditPet.route) },
                            petViewModel = petViewModel,
                            onNavigateToAdminAddPet = { navController.navigate(AdminPetDestinations.AdminAddPet.route) }
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminPetDestinations.AdminAddPet.route) {
                AdminPetModule(
                    content = {
                        AdminAddPetScreen(
                            onNavigateToPetList = { navController.popBackStack() },
                            petViewModel = petViewModel
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }

            composable(route = AdminPetDestinations.AdminEditPet.route) {
                AdminPetModule(
                    content = {
                        AdminPetDetailsScreen(
                            onNavigateToPetList = { navController.navigate(AdminPetDestinations.AdminPetList.route) },
                            petViewModel = petViewModel
                        )
                    },
                    onNavigateBack = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(AppDestinations.AdminHome.route) {
                            popUpTo(AppDestinations.AdminHome.route) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                    onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                    onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                    onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                    onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                    onNavigateToProfile = {},
                    onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
                )
            }
        }

        // Booking Module
        composable(route = AppDestinations.AdminBooking.route) {
            AdminBookingModule(
                onNavigateBack = {
                    navController.navigate(AppDestinations.AdminHome.route) {
                        popUpTo(AppDestinations.AdminHome.route) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToCommunity = { navController.navigate(AppDestinations.AdminCommunity.route) },
                onNavigateToHome = {
                    navController.navigate(AppDestinations.AdminHome.route) {
                        popUpTo(AppDestinations.AdminHome.route) {
                            inclusive = true
                        }
                    }
                },
                bookingViewModel = bookingViewModel,
                content = {
                    AdminBookingScreen(
                        bookingViewModel = bookingViewModel
                    )
                },
                onNavigateToPetList = { navController.navigate(AppDestinations.AdminPet.route) },
                onNavigateToBookingList = { navController.navigate(AppDestinations.AdminBooking.route) },
                onNavigateToReport = { navController.navigate(AppDestinations.AdminReport.route) },
                onNavigateToDonation = { navController.navigate(AppDestinations.AdminDonation.route) },
                onNavigateToProfile = {},
                onNavigateToAboutUs = { navController.navigate(AppDestinations.AdminAboutUs.route) }
            )
        }
    }
}
private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}