package com.example.mayChatroomApp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.mayChatroomApp.ui.theme.ChatroomAppTheme
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController= rememberNavController()
            val authViewModel: AuthViewModel= viewModel()
            ChatroomAppTheme {
                Surface (Modifier.padding(16.dp).fillMaxSize(),
                    color =MaterialTheme.colorScheme.background){
                    Navigation(navController , authViewModel = authViewModel)
                }
                }
            }
        }
    }




