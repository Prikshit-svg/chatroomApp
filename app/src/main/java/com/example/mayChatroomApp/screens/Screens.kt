package com.example.mayChatroomApp.screens

sealed class Screen(val route:String) {
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("signup_screen")
    object ChatScreen : Screen("chat_screen")
    object ChatRoomsScreen:Screen("chat_room_screen")
}