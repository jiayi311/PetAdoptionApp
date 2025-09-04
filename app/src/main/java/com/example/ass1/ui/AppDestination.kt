package com.example.ass1.ui

// Login/Register Module Destinations
sealed class LoginRegisterDestinations {
    object Start : LoginRegisterDestinations()
    object Login : LoginRegisterDestinations()
    object Register : LoginRegisterDestinations()
    object ForgotPassword : LoginRegisterDestinations()
}

val LoginRegisterDestinations.route: String
    get() = when (this) {
        is LoginRegisterDestinations.Start -> "started_page"
        is LoginRegisterDestinations.Login -> "login"
        is LoginRegisterDestinations.Register -> "register"
        is LoginRegisterDestinations.ForgotPassword -> "forgot_password"
    }

// Report Module Destinations
sealed class ReportDestinations {
    object ReportList : ReportDestinations()
    object ReportHistory : ReportDestinations()
}

val ReportDestinations.route: String
    get() = when (this) {
        is ReportDestinations.ReportList -> "report_list"
        is ReportDestinations.ReportHistory -> "report_history"
    }

// Donation Module Destinations
sealed class DonationDestinations {
    object DonationList : DonationDestinations()
    object DonationHistory : DonationDestinations()
    object DonationExpenses : DonationDestinations()
}

val DonationDestinations.route: String
    get() = when (this) {
        is DonationDestinations.DonationList -> "donation_list"
        is DonationDestinations.DonationHistory -> "donation_history"
        is DonationDestinations.DonationExpenses -> "donation_expenses"
    }

// Community Module Destinations
sealed class CommunityDestinations {
    object CommunityList : CommunityDestinations()
    object PostDetails : CommunityDestinations()
}

val CommunityDestinations.route: String
    get() = when (this) {
        is CommunityDestinations.CommunityList -> "community_list"
        is CommunityDestinations.PostDetails -> "post_details"
    }

// Pet Module Destinations
sealed class PetDestinations {
    object PetList : PetDestinations()
    object PetDetails : PetDestinations()
}

val PetDestinations.route: String
    get() = when (this) {
        is PetDestinations.PetList -> "pet_list"
        is PetDestinations.PetDetails -> "pet_details"
    }

// Booking Module Destinations
sealed class BookingDestinations {
    object BookingList : BookingDestinations()
    object BookingHistory : BookingDestinations()
}

val BookingDestinations.route: String
    get() = when (this) {
        is BookingDestinations.BookingList -> "booking_list"
        is BookingDestinations.BookingHistory -> "booking_history"
    }

// Profile Module Destinations
sealed class ProfileDestinations {
    object Profile : ProfileDestinations()
    object EditProfile : ProfileDestinations()
    object ChangePassword : ProfileDestinations()
}

val ProfileDestinations.route: String
    get() = when (this) {
        is ProfileDestinations.Profile -> "user_profile"
        is ProfileDestinations.EditProfile -> "edit_profile"
        is ProfileDestinations.ChangePassword -> "change_password"
    }

// admin part
//AboutUs Module Destinations
sealed class AdminAboutUsDestinations {
    object AdminAboutUsView : AdminAboutUsDestinations()
    object AdminAboutUsEdit : AdminAboutUsDestinations()
}

val AdminAboutUsDestinations.route: String
    get() = when (this) {
        is AdminAboutUsDestinations.AdminAboutUsView -> "admin_aboutus_view"
        is AdminAboutUsDestinations.AdminAboutUsEdit -> "admin_aboutus_edit"
    }

// Donation Module Destinations
sealed class AdminDonationDestinations {
    object AdminDonationList : AdminDonationDestinations()
    object AdminExpensesList : AdminDonationDestinations()
    object AdminExpensesAdd : AdminDonationDestinations()
    object AdminExpensesEdit : AdminDonationDestinations()
}

val AdminDonationDestinations.route: String
    get() = when (this) {
        is AdminDonationDestinations.AdminDonationList -> "admin_donation_list"
        is AdminDonationDestinations.AdminExpensesList -> "admin_expenses_list"
        is AdminDonationDestinations.AdminExpensesAdd -> "admin_expenses_add"
        is AdminDonationDestinations.AdminExpensesEdit -> "admin_expenses_edit"
    }

// Community Module Destinations
sealed class AdminCommunityDestinations {
    object AdminCommunityList : AdminCommunityDestinations()
    object AdminEditPost : AdminCommunityDestinations()
    object AdminAddPost : AdminCommunityDestinations()
    object AdminViewPost : AdminCommunityDestinations()
}

val AdminCommunityDestinations.route: String
    get() = when (this) {
        is AdminCommunityDestinations.AdminCommunityList -> "admin_community_list"
        is AdminCommunityDestinations.AdminEditPost -> "admin_edit_post"
        is AdminCommunityDestinations.AdminAddPost -> "admin_add_post"
        is AdminCommunityDestinations.AdminViewPost -> "admin_view_post"
    }

// Pet Module Destinations
sealed class AdminPetDestinations {
    object AdminPetList : AdminPetDestinations()
    object AdminEditPet : AdminPetDestinations()
    object AdminAddPet : AdminPetDestinations()
}

val AdminPetDestinations.route: String
    get() = when (this) {
        is AdminPetDestinations.AdminPetList -> "admin_pet_list"
        is AdminPetDestinations.AdminEditPet -> "admin_edit_pet"
        is AdminPetDestinations.AdminAddPet -> "admin_add_pet"
    }
