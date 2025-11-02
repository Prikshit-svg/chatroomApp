package com.example.mayChatroomApp

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mayChatroomApp.screens.ChatRoomList
import com.example.mayChatroomApp.screens.ChatScreen
import com.example.mayChatroomApp.screens.LoginScreen
import com.example.mayChatroomApp.screens.Screen
import com.example.mayChatroomApp.screens.SignUpScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navHostController : NavHostController,
               authViewModel: AuthViewModel
               ){

   /* val startDestination= if(FirebaseAuth.getInstance().currentUser!=null){
        Screen.ChatRoomsScreen.route

    }else{
        Screen.SignUpScreen.route
    }

    */

    NavHost(navHostController, Screen.SignUpScreen.route) {

        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(
                onNavigateToLogin = {
                    navHostController.navigate(Screen.LoginScreen.route)

                },
                authViewModel = authViewModel,
                onNavigateToRoomList ={}
            )

        }
        composable(Screen.LoginScreen.route){
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = {
                    navHostController.navigate(Screen.SignUpScreen.route)
                })
                 {
                    navHostController.navigate(Screen.ChatRoomsScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }


        }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomList {
                navHostController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }
        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomList { room ->
                // Check if room ID is valid before navigating
                if (room.id.isNotEmpty()) {
                    navHostController.navigate("${Screen.ChatScreen.route}/${room.id}")
                } else {
                    Log.e("Navigation", "Invalid room ID: ${room.id}")
                }
            }
        }
    }
}