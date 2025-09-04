package com.example.ass1.ui.screen.donation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ass1.model.AdminBookingHistory
import com.example.ass1.model.AdminDonationHistory
import com.example.ass1.model.Donation
import com.example.ass1.model.ExpenseInFirebase
import com.example.ass1.model.ExpenseItem
import com.example.ass1.model.Pet
import com.example.ass1.model.Receipt
import com.example.ass1.model.ReceiptItem
import com.example.ass1.model.ReceiptItemInFirebase
import com.example.ass1.model.UploadedImage
import com.example.ass1.ui.screen.profile.ProfileViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DonationViewModel(
    private val profileViewModel: ProfileViewModel
): ViewModel() {

    private val _uiState = MutableStateFlow(DonationUiState())
    val uiState: StateFlow<DonationUiState> = _uiState.asStateFlow()

    private var auth: FirebaseAuth =  FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val donationsCollection = db.collection("Donation")
    private val expensesCollection = db.collection("Expenses")
    private val receiptItemCollection = db.collection("ReceiptItem")

    private val _donationCount = MutableStateFlow(0)
    private val _donationsList = MutableStateFlow<List<Donation>>(emptyList())
    val donationList: MutableStateFlow<List<Donation>> = _donationsList

    private val _userDonationList = MutableStateFlow<List<Donation>>(emptyList())
    val userDonationList: StateFlow<List<Donation>> = _userDonationList.asStateFlow()

    private val _adminDonationList = MutableStateFlow<List<AdminDonationHistory>>(emptyList())
    val adminDonationList: StateFlow<List<AdminDonationHistory>> = _adminDonationList.asStateFlow()

    private val _expensesCount = MutableStateFlow(0)
    private val _expensesList = MutableStateFlow<List<ExpenseInFirebase>>(emptyList())
    val expensesList: MutableStateFlow<List<ExpenseInFirebase>> = _expensesList

    private val _receiptItemCount = MutableStateFlow(0)
    private val _receiptItemList = MutableStateFlow<List<ReceiptItemInFirebase>>(emptyList())
    val receiptItemList: MutableStateFlow<List<ReceiptItemInFirebase>> = _receiptItemList

    private val _expensesItemList = MutableStateFlow<List<ExpenseItem>>(emptyList())
    val expensesItemList: MutableStateFlow<List<ExpenseItem>> = _expensesItemList



    init{
        listenForDonations()
        donationHistoryList()
        listenForExpensesInFirebase()
        listenForReceiptItemInFirebase()

        viewModelScope.launch {
            profileViewModel.currentUser.collect { user ->
                filterUserDonations(user.userId)
            }
        }
    }

    fun calculateTotal() {
        var totalAmt = 0.0
        for((index) in _uiState.value.newReceiptItemList.withIndex()) {
            totalAmt += _uiState.value.newReceiptItemList[index].price
        }

        _uiState.value = _uiState.value.copy(
            newExpenses = _uiState.value.newExpenses.copy(
                amount = totalAmt
            )
        )
    }

    fun updateSelectedDate(date: Long) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date
        )
        updateCombinedTimestamp()
    }

    fun updateSelectedTime(time: Long) {
        _uiState.value = _uiState.value.copy(
            selectedTime = time
        )
        updateCombinedTimestamp()
    }

    private fun updateCombinedTimestamp() {
        val dateCalendar = Calendar.getInstance().apply {
            timeInMillis = _uiState.value.selectedDate
        }

        val timeCalendar = Calendar.getInstance().apply {
            timeInMillis = _uiState.value.selectedTime
        }

        // Create a new calendar with date from dateCalendar and time from timeCalendar
        val combinedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR))
            set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH))

            set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
            set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Convert to Firebase Timestamp
        val combineTimestamp = Timestamp(Date(combinedCalendar.timeInMillis))

        _uiState.value = _uiState.value.copy(
            newExpenses = _uiState.value.newExpenses.copy(
                receipt = _uiState.value.newExpenses.receipt.copy(
                    timestamp = combineTimestamp
                )
            )
        )
    }

    fun updateExpPaymentMethod(pm: String) {
        _uiState.value = _uiState.value.copy(
            newExpenses = _uiState.value.newExpenses.copy(
                receipt = _uiState.value.newExpenses.receipt.copy(
                    paymentMethod = pm
                )
            )
        )
    }

    fun updateExpCategory(cat: String) {
        _uiState.value = _uiState.value.copy(
            newExpenses = _uiState.value.newExpenses.copy(
                category = cat
            )
        )
    }

    fun updateReceiptItemListPrice(price: Double, index: Int) {
        // Get the current list
        val currentList = _uiState.value.newReceiptItemList.toMutableList()

        // Update the item at the specified index
        val updatedItem = currentList[index].copy(price = price)
        currentList[index] = updatedItem

        // Update the state with the new list
        _uiState.value = _uiState.value.copy(
            newReceiptItemList = currentList
        )
    }

    fun updateReceiptItemListName(name: String, index: Int) {
        // Get the current list
        val currentList = _uiState.value.newReceiptItemList.toMutableList()

        // Update the item at the specified index
        val updatedItem = currentList[index].copy(name = name)
        currentList[index] = updatedItem

        // Update the state with the new list
        _uiState.value = _uiState.value.copy(
            newReceiptItemList = currentList
        )
    }

    fun addImages(newImage: UploadedImage) {
        _uiState.update { currentState ->
            currentState.copy(
                newExpenses = _uiState.value.newExpenses.copy(
                    imageUrl = newImage.base64Data
                ),
                imageName = newImage.name
            )
        }
    }

    fun uploadImages(newImage: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newExpenses = _uiState.value.newExpenses.copy(
                    imageUrl = newImage
                ),
            )
        }
    }

    fun removeImage() {
        _uiState.update { currentState ->
            currentState.copy(
                newExpenses = _uiState.value.newExpenses.copy(
                    imageUrl = ""
                ),
                imageName = ""
            )
        }
    }

    fun updateEditImg() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val updateExp = ExpenseInFirebase(
                expensesId = _uiState.value.selectedExpenses.id,
                amount = _uiState.value.selectedExpenses.amount,
                category = _uiState.value.selectedExpenses.category,
                imageUrl = _uiState.value.selectedExpenses.imageUrl,
                expensesDate = _uiState.value.selectedExpenses.receipt.timestamp,
                paymentMethod = _uiState.value.selectedExpenses.receipt.paymentMethod
            )

            try{
                val querySnapshot = db.collection("Expenses")
                    .whereEqualTo("expensesId", updateExp.expensesId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Expenses").document(documentId)
                        .set(updateExp)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Expenses not found in database"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to update expenses: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true
                )
            }
        }
    }

    fun donationHistoryList() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                // Create a temporary mutable list to hold results
                val tempAdminDonationList = mutableListOf<AdminDonationHistory>()

                // Process each booking sequentially to avoid race conditions
                for(dn in _donationsList.value) {
                    try {
                        val userDoc = db.collection("User")
                            .whereEqualTo("userId", dn.donatorId)
                            .get()
                            .await()

                        if (userDoc.documents.isNotEmpty()) {
                            val user = userDoc.documents[0]

                            val gender = user.getLong("userGender") ?: 0

                            val adminDonationHistory = AdminDonationHistory(
                                donation = dn,
                                gender = gender.toInt()

                            )

                            // Add to our temporary list
                            tempAdminDonationList.add(adminDonationHistory)
                        }
                    } catch (e: Exception) {
                        Log.e("Donation", "Error retrieving user info for donation ${dn.donatorId}", e)
                    }
                }

                // Update the StateFlow with the complete new list
                _adminDonationList.value = tempAdminDonationList

                // Set loading to false after all processing is complete
                _uiState.value = _uiState.value.copy(isLoading = false)

            } catch (e: Exception) {
                Log.e("Donation", "Error in donationHistoryList", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    fun getCurrentDonateAmount(): Double {
        return _uiState.value.donateAmount
    }

    fun resetDonationForm() {
        _uiState.value = _uiState.value.copy(
            donateTime = Timestamp.now(),
            donateAmount = 0.0,
            errorMessage = null,
            isLoading = false,
            isDonate = false,
            isDonationSuccessful = false,
            selectedButton = 0,
            customAmount = 0.0,
            donatorId = "",
            donationId = "",
            isEdit = false,
            isEditSuccessful = false,
            imageName = "",
            newExpenses = ExpenseItem(),
            newReceiptItemList = List(5) { ReceiptItem() },
            selectedDate = 0,
            selectedTime = 0,
        )
    }

    fun updateAmount() {

        if(_uiState.value.selectedButton == 0 && _uiState.value.customAmount != 0.0) {
            _uiState.value = _uiState.value.copy(donateAmount = _uiState.value.customAmount)
        } else {
            when(_uiState.value.selectedButton) {
                1 -> _uiState.value = _uiState.value.copy(donateAmount = 5.00)
                2 -> _uiState.value = _uiState.value.copy(donateAmount = 10.00)
                3 -> _uiState.value = _uiState.value.copy(donateAmount = 20.00)
                4 -> _uiState.value = _uiState.value.copy(donateAmount = 30.00)
                5 -> _uiState.value = _uiState.value.copy(donateAmount = 40.00)
                6 -> _uiState.value = _uiState.value.copy(donateAmount = 50.00)
                else -> _uiState.value = _uiState.value.copy(donateAmount = 0.00, errorMessage = "Donate Amount should not be 0")
            }
        }
    }

    fun updateCustomAmount(amount: Double) {
        _uiState.value = _uiState.value.copy(
            customAmount = amount,
            selectedButton = 0
        )
    }

    fun updateSelectedButton(button: Int) {
        _uiState.value = _uiState.value.copy(
            selectedButton = button,
            customAmount = 0.0
        )
    }

    fun updateSelectedExpenses(exp: ExpenseItem) {
        _uiState.value = _uiState.value.copy(
            selectedExpenses = exp
        )
    }

    fun updateSelectedImage(img: String) {
        _uiState.value = _uiState.value.copy(
            selectedExpenses = _uiState.value.selectedExpenses.copy(
                imageUrl = img
            )
        )
    }

    fun assignExpToUiState() {
        _uiState.value = _uiState.value.copy(
            newExpenses = _uiState.value.selectedExpenses,
            newReceiptItemList = _uiState.value.selectedExpenses.receipt.items
        )

        if(_uiState.value.newReceiptItemList.size == 1) {

            val currentList = _uiState.value.newReceiptItemList.toMutableList()
            currentList.add(1,ReceiptItem("",0.0))
            currentList.add(2,ReceiptItem("",0.0))
            currentList.add(3,ReceiptItem("",0.0))
            currentList.add(4,ReceiptItem("",0.0))
            _uiState.value = _uiState.value.copy(
                newReceiptItemList =  currentList
            )

        }

        if(_uiState.value.newReceiptItemList.size == 2) {

            val currentList = _uiState.value.newReceiptItemList.toMutableList()
            currentList.add(2,ReceiptItem("",0.0))
            currentList.add(3,ReceiptItem("",0.0))
            currentList.add(4,ReceiptItem("",0.0))
            _uiState.value = _uiState.value.copy(
                newReceiptItemList =  currentList
            )
        }

        if(_uiState.value.newReceiptItemList.size == 3) {

            val currentList = _uiState.value.newReceiptItemList.toMutableList()
            currentList.add(3,ReceiptItem("",0.0))
            currentList.add(4,ReceiptItem("",0.0))
            _uiState.value = _uiState.value.copy(
                newReceiptItemList =  currentList
            )

        }

        if(_uiState.value.newReceiptItemList.size == 4) {

            val currentList = _uiState.value.newReceiptItemList.toMutableList()
            currentList.add(4,ReceiptItem("",0.0))
            _uiState.value = _uiState.value.copy(
                newReceiptItemList =  currentList
            )

        }

    }

    fun updateTabState(newTab: Int) {
        _uiState.update { currentState ->
            currentState.copy(tabState = newTab)
        }
    }

    private fun listenForDonations() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Donation")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for donations: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val donationsList = snapshot.documents.mapNotNull { document ->
                        document.toObject(Donation::class.java)
                    }
                    val sortedList = donationsList.sortedByDescending { it.donationId }
                    _donationsList.value = sortedList
                    _donationCount.value = _donationsList.value.size
                    filterUserDonations(profileViewModel.currentUser.value.userId)
                }
            }

    }

    private fun listenForExpensesInFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Expenses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for expenses: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val expensesList = snapshot.documents.mapNotNull { document ->
                        document.toObject(ExpenseInFirebase::class.java)
                    }
                    _expensesList.value = expensesList
                    _expensesCount.value = _expensesList.value.size
                }
            }
    }

    private fun listenForReceiptItemInFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("ReceiptItem")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to listen for receipt item: ${error.message}"
                    )
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val receiptItemList = snapshot.documents.mapNotNull { document ->
                        document.toObject(ReceiptItemInFirebase::class.java)
                    }
                    _receiptItemList.value = receiptItemList
                    _receiptItemCount.value = _receiptItemList.value.size
                }
            }
    }

    fun fetchExpenseItemList() {
        val expensesItemList = getExpensesItemList(
            expensesList = expensesList.value,
            receiptItems = receiptItemList.value
        )
        _expensesItemList.value = expensesItemList
    }

    private fun autoGenerateDonationId(): String {
        val currentDonationCount = _donationCount.value + 1
        _donationCount.value = currentDonationCount
        val newDonationId = String.format(Locale.ROOT,"DN%03d", currentDonationCount)
        return newDonationId
    }

    private fun autoGenerateExpensesId(): String {
        val currentExpensesCount = _expensesCount.value + 1
        _expensesCount.value = currentExpensesCount
        val newExpensesId = String.format(Locale.ROOT,"EP%04d", currentExpensesCount)
        return newExpensesId
    }

    fun submitNewDonation() {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                // Get current user from ProfileViewModel
                val currentUser = profileViewModel.currentUser.value
                val currentUserId = currentUser.userId
                
                val donationObj = Donation(
                    donatorId = currentUserId,
                    donationId = autoGenerateDonationId(),
                    donateTime = _uiState.value.donateTime,
                    donateAmount = _uiState.value.donateAmount
                )

                donationsCollection.add(donationObj).await()

                _uiState.value = _uiState.value.copy(
                    isDonationSuccessful = true
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDonationSuccessful = false,
                    errorMessage = "Failed to donate: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isDonate = true,
                )
            }
        }
    }

    fun validateDonationForm(): Boolean {
        var returnValue: Boolean = true

        if (_uiState.value.selectedButton == 0) {
            if(_uiState.value.customAmount == 0.00) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Amount fields is required."
                )
                returnValue = false
            } else if (_uiState.value.customAmount < 0.0) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Amount should not be negative. Please enter a valid amount."
                )
                returnValue = false
            }
        }
        return returnValue
    }

    fun submitNewExpenses() {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {

                val expensesObj = ExpenseInFirebase(
                    expensesId = autoGenerateExpensesId(),
                    amount = _uiState.value.newExpenses.amount,
                    category = _uiState.value.newExpenses.category,
                    imageUrl = _uiState.value.newExpenses.imageUrl,
                    expensesDate = _uiState.value.newExpenses.receipt.timestamp,
                    paymentMethod = _uiState.value.newExpenses.receipt.paymentMethod
                )

                expensesCollection.add(expensesObj).await()

                _uiState.value.newReceiptItemList.forEach { item ->
                    try {
                        val receiptItem = ReceiptItemInFirebase(
                            expensesId = expensesObj.expensesId,
                            itemName = item.name,
                            itemPrice = item.price,
                        )

                        if(receiptItem.itemName.isNotBlank()) {
                            receiptItemCollection.add(receiptItem).await()
                        }

                    }catch (e: Exception){
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Failed to update receipt item list: ${e.message}"
                        )
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = true
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = false,
                    errorMessage = "Failed to add expenses: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true,
                )
            }
        }
    }

    fun submitEditExpenses() {

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {

                val expensesObj = ExpenseInFirebase(
                    expensesId = _uiState.value.newExpenses.id,
                    amount = _uiState.value.newExpenses.amount,
                    category = _uiState.value.newExpenses.category,
                    imageUrl = _uiState.value.newExpenses.imageUrl,
                    expensesDate = _uiState.value.newExpenses.receipt.timestamp,
                    paymentMethod = _uiState.value.newExpenses.receipt.paymentMethod
                )

                val querySnapshot = db.collection("Expenses")
                    .whereEqualTo("expensesId", expensesObj.expensesId)
                    .get()
                    .await()

                if (querySnapshot.documents.isNotEmpty()) {
                    val documentId = querySnapshot.documents[0].id

                    db.collection("Expenses").document(documentId)
                        .set(expensesObj)
                        .await()
                    _uiState.value = _uiState.value.copy(
                        isEditSuccessful = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Expenses not found in database"
                    )
                }

                val querySnapshot1 = db.collection("ReceiptItem")
                    .whereEqualTo("expensesId", expensesObj.expensesId)
                    .get()
                    .await()

                if(!querySnapshot1.isEmpty) {
                    for (document in querySnapshot1.documents) {
                        db.collection("ReceiptItem")
                            .document(document.id)
                            .delete()
                            .await()
                    }
                }

                _uiState.value.newReceiptItemList.forEach { item ->
                    try {
                        val receiptItem = ReceiptItemInFirebase(
                            expensesId = expensesObj.expensesId,
                            itemName = item.name,
                            itemPrice = item.price,
                        )

                        if(receiptItem.itemName.isNotBlank()) {
                            receiptItemCollection.add(receiptItem).await()
                        }

                    }catch (e: Exception){
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Failed to update receipt item list: ${e.message}"
                        )
                    }
                }

                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = true
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isEditSuccessful = false,
                    errorMessage = "Failed to add expenses: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isEdit = true,
                )
            }
        }
    }

    fun validateAddExpensesForm(): Boolean {
        var returnValue: Boolean = true
        var blankItem = 0

        _uiState.value.newReceiptItemList.forEach { item ->

            if(item.name.isBlank() && item.price == 0.0) {
                blankItem ++
            } else if(item.name.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Item name is required."
                )
                returnValue = false
            }

            if(_uiState.value.newReceiptItemList.size == 0){
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Item is required."
                )
                returnValue = false
            }
        }

        if(blankItem == 5) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Item is required."
            )
            returnValue = false
        }

        if(_uiState.value.newExpenses.imageUrl.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Image is required for evidence."
            )
            returnValue = false
        }

        if(_uiState.value.newExpenses.category.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Expenses category is required."
            )
            returnValue = false
        }

        if(_uiState.value.newExpenses.receipt.paymentMethod.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Payment Method is required."
            )
            returnValue = false
        }

        return returnValue
    }

    fun formatTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun formatTimestampToTime(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun getDayOfWeekFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val dayFormatter = SimpleDateFormat("EEE", Locale.ENGLISH)
        return dayFormatter.format(date)
    }

    fun getFormattedDateFromTimestamp(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        return formatter.format(date)
    }

    fun convertToExpenseItem(
        expense: ExpenseInFirebase,
        receiptItems: List<ReceiptItemInFirebase>
    ): ExpenseItem {
        // Filter receipt items that belong to this expense
        val filteredItems = receiptItems.filter { it.expensesId == expense.expensesId }

        // Convert ReceiptItemInFirebase to ReceiptItem
        val items = filteredItems.map { firebaseItem ->
            ReceiptItem(
                name = firebaseItem.itemName,
                price = firebaseItem.itemPrice
            )
        }

        // Create Receipt object
        val receipt = Receipt(
            items = items,
            paymentMethod = expense.paymentMethod,
            timestamp = expense.expensesDate
        )

        // Create and return ExpenseItem
        return ExpenseItem(
            id = expense.expensesId,
            amount = expense.amount,
            category = expense.category,
            receipt = receipt,
            imageUrl = expense.imageUrl
        )
    }

    fun getExpensesItemList(
        expensesList: List<ExpenseInFirebase>,
        receiptItems: List<ReceiptItemInFirebase>
    ): List<ExpenseItem> {
        return expensesList.map { expense ->
            convertToExpenseItem(
                expense = expense,
                receiptItems = receiptItems
            )
        }
    }

    private fun filterUserDonations(userId: String) {
        // Add null check for userId
        if (userId.isBlank()) {
            return
        }
        
        // Create a new list with the filtered donations
        val filteredList = _donationsList.value.filter { donation ->
            // Add null check for donation.donatorId
            val donatorId = donation.donatorId ?: ""
            val matches = donatorId == userId
            matches
        }
        _userDonationList.value = filteredList
    }

    class DonationViewModelFactory(
        private val profileViewModel: ProfileViewModel
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DonationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DonationViewModel(profileViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}



