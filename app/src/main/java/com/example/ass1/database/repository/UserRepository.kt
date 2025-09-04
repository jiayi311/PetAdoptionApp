package com.example.ass1.data.repository

import android.util.Log
import com.example.ass1.database.dao.UserDao
import com.example.ass1.database.UserEntity
import com.example.ass1.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("User")

    // Convert between domain model and entity
    private fun User.toEntity(): UserEntity {
        return UserEntity(
            userId = userId,
            userName = userName,
            userMail = userMail,
            userGender = userGender,
            userPhone = userPhone
        )
    }

    private fun UserEntity.toDomainModel(): User {
        return User(
            userId = userId,
            userName = userName,
            userMail = userMail,
            userGender = userGender,
            userPhone = userPhone
        )
    }

    // Get user from local database by email
    fun getUserByEmailStream(email: String): Flow<User?> {
        return userDao.getUserByEmail(email).map { entity ->
            entity?.toDomainModel()
        }
    }

    // Save user to local database
    suspend fun saveUserToLocal(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user.toEntity())
        }
    }

    // Fetch user from Firebase by email
    suspend fun fetchUserByEmail(email: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = usersCollection.whereEqualTo("userMail", email).get().await()

                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    Log.d("UserRepository", "Document found: ${document.id}")

                    val user = User(
                        userId = document.getString("userId") ?: "",
                        userName = document.getString("userName") ?: "",
                        userPhone = document.getString("userPhone") ?: "",
                        userGender = document.getLong("userGender")?.toInt() ?: 0,
                        userMail = document.getString("userMail") ?: ""
                    )

                    // Save to local database
                    saveUserToLocal(user)

                    Result.success(user)
                } else {
                    Log.d("UserRepository", "No document found for email: $email")
                    Result.failure(Exception("User not found"))
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error fetching user data", e)
                Result.failure(e)
            }
        }
    }

    // Update user in Firebase
    suspend fun updateUserInFirebase(user: User): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // First find the document by email
                val querySnapshot = usersCollection.whereEqualTo("userMail", user.userMail).get().await()

                if (!querySnapshot.isEmpty) {
                    val documentId = querySnapshot.documents[0].id

                    // Update the document
                    val updatedData = mapOf(
                        "userName" to user.userName,
                        "userGender" to user.userGender,
                        "userPhone" to user.userPhone
                    )

                    usersCollection.document(documentId).update(updatedData).await()

                    // Also update local database
                    saveUserToLocal(user)

                    Result.success(Unit)
                } else {
                    Result.failure(Exception("User not found in Firebase"))
                }
            } catch (e: Exception) {
                Log.e("UserRepository", "Error updating user in Firebase", e)
                Result.failure(e)
            }
        }
    }

    // Clear user data (for logout)
    suspend fun clearUserData() {
        withContext(Dispatchers.IO) {
            userDao.deleteAllUsers()
        }
    }
}