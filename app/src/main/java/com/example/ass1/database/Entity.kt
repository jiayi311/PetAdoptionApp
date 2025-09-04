package com.example.ass1.database
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val userId: String,
    val userName: String,
    val userMail: String,
    val userGender: Int,
    val userPhone: String
)