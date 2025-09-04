package com.example.ass1.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

data class TabItem(
    val title: String,
    val route: String,
    val command: () -> Unit
)

data class Module(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

data class AboutUs(
    var mission: String,
    var operationHour: String,
    var phoneNumber: String,
    var email: String,
    var address: String
){
    constructor(): this(
        mission = "",
        operationHour = "",
        phoneNumber = "",
        email = "",
        address = ""
    )
}

data class AdminDonationHistory(
    val donation: Donation,
    val gender: Int
){
    constructor(): this(
        donation = Donation(),
        gender = 0
    )
}

data class AdminBookingHistory(
    val booking: Booking,
    val phone: String
){
    constructor(): this(
        booking = Booking(),
        phone = ""
    )
}

data class BookingDate(
    val day: String,
    val month: String
){
    constructor(): this(
        day = "",
        month = ""
    )
}

data class Pet(
    var age: Int,
    var breed: String,
    var description: String,
    var deepDescription: String,
    var image1: String,
    var image2: String,
    var petId: String,
    var petName: String,
){
    constructor(): this(
        age = 0,
        breed = "",
        description = "",
        deepDescription = "",
        image1 = "",
        image2 = "",
        petId = "",
        petName = "",
    )
}

data class Event(
    val eventId: String,
    var eventTitle: String,
    val postedDate: Timestamp,
    var eventDetails: String,
    var imageUrl: String
){
    constructor(): this(
        eventId = "",
        eventTitle = "",
        postedDate = Timestamp.now(),
        eventDetails = "",
        imageUrl = ""
    )
}

data class ExpenseInFirebase(
    val expensesId: String,
    val amount: Double,
    val category: String,
    val imageUrl: String,
    val expensesDate: Timestamp,
    val paymentMethod: String

){
    constructor(): this(
        expensesId = "",
        amount = 0.0,
        category = "",
        imageUrl = "",
        expensesDate = Timestamp.now(),
        paymentMethod = ""
    )
}

data class ReceiptItemInFirebase(
    val expensesId: String,
    val itemPrice: Double,
    val itemName: String,

){
    constructor(): this(
        expensesId = "",
        itemPrice = 0.0,
        itemName = ""
    )
}

data class ExpenseItem(
    val id: String,
    val amount: Double,
    val category: String,
    val receipt: Receipt,
    val imageUrl: String
){
    constructor(): this(
        id = "",
        amount = 0.0,
        category = "",
        receipt = Receipt(),
        imageUrl = ""
    )
}

data class Receipt(
    val items: List<ReceiptItem>,
    val paymentMethod: String,
    val timestamp: Timestamp
){
    constructor(): this(
        items = listOf(ReceiptItem()),
        paymentMethod = "",
        timestamp = Timestamp.now()
    )
}

data class ReceiptItem(
    val name: String,
    val price: Double
){
    constructor(): this(
        name = "",
        price = 0.0
    )
}

data class UploadedImage(
    val name: String,
    val base64Data: String
){
    constructor(): this(
        name = "",
        base64Data = ""
    )
}

data class User(
    var userId: String,
    var userName: String,
    var userMail: String,
    var userPhone: String,
    var userGender: Int
    // by default is unknown = 0
    // user can set it to male = 1
    // and female = 2
) {
    constructor() : this(
        userId = "",
        userName = "",
        userMail = "",
        userPhone = "",
        userGender = 0

    )
}

data class Report(
    val adminComment: String,
    val details: String,
    val location: String,
    val reportCaseId: String,
    val reportTime: Timestamp,
    val reporterId: String,
    val status:Int,
    val reportImage: String,
    val reportImageName: String
    // assume user can only upload one picture
    // by default is pending status = 0
    // admin will set the report to process = 1
    // and complete = 2
) {
    constructor() : this(
        adminComment = "",
        details = "",
        location = "",
        reportCaseId = "",
        reportTime = Timestamp.now(),
        reporterId = "",
        status = 0,
        reportImage = "",
        reportImageName = ""

    )
}

data class Donation(
    val donatorId: String,
    val donationId: String,
    val donateTime: Timestamp,
    val donateAmount: Double
) {
    constructor() : this(
        donatorId = "",
        donationId = "",
        donateTime = Timestamp.now(),
        donateAmount = 0.0
    )
}

data class Booking(
    val bookerId: String,
    val bookingId: String,
    val bookingReason: String,
    val bookingTime: Timestamp,
    val bookDate: Timestamp,
    val status: Int
    // by default is pending status = 0
    // admin will set the approve = 1
    // and reject = 2
    // user can cancel booking = 3
) {
    constructor() : this(
        bookerId = "",
        bookingId = "",
        bookingReason = "",
        bookingTime = Timestamp.now(),
        bookDate = Timestamp.now(),
        status = 0
    )
}
