package com.example.ass1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.ass1.data.repository.UserRepository
import com.example.ass1.database.AppDatabase
import com.example.ass1.ui.AppNavigation
import com.example.ass1.ui.screen.loginRegister.LoginRegisterModule
import com.example.ass1.ui.screen.loginRegister.LoginRegisterViewModel
import com.example.ass1.ui.screen.loginRegister.LoginScreen
import com.example.ass1.ui.screen.loginRegister.RegisterScreen
import com.example.ass1.ui.screen.report.ReportModule
import com.example.ass1.ui.theme.Ass1Theme

class MainActivity : ComponentActivity() {
    // Database instance
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // Repository instance
    val userRepository by lazy {
        UserRepository(database.userDao())
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ass1Theme {
                AppNavigation()
            }
        }
    }
}

