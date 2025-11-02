package com.example.mayChatroomApp.data

data class Message(
    val senderId:String="",
    val senderName: String="",
    val text: String="",
    val timestamp: Long= System.currentTimeMillis(),
    val isSentByCurrentUser: Boolean = false


)
