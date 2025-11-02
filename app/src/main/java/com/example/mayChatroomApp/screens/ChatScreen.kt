package com.example.mayChatroomApp.screens
import java.time.format.DateTimeFormatter
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mayChatroomApp.MessageViewModel
import com.example.mayChatroomApp.data.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import com.example.mayChatroomApp.R
import com.example.mayChatroomApp.toastMessage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(roomId: String,
               messageViewModel:
               MessageViewModel = viewModel()
               ){

    val context= LocalContext.current
    val text = remember { mutableStateOf("") }
    val message by messageViewModel.messages.observeAsState(emptyList())
    LaunchedEffect(key1 = roomId) {
        messageViewModel.setRoomId(roomId)
    }
    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        //display the messages
        LazyColumn(Modifier.weight(1f)) {
            items(message){
                message->
                ChatMessageItem(message.copy(isSentByCurrentUser =message.senderId== messageViewModel.currentUser.value?.email))
            }
        }
        //text area and send icon
        Row (Modifier
            .padding(16.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
            BasicTextField(value = text.value, onValueChange = {text.value=it},
                textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                modifier = Modifier.weight(1f))

            IconButton(onClick = {
                if(text.value.isNotEmpty()){
                    //send the message
                    messageViewModel.sendMessage(text.value.trim())
                    text.value=""//Clear the text field after sending
                }

            }) {
                Icon(Icons.Default.Send,contentDescription = "send the message")
            }
            if(toastMessage.value){
                Toast.makeText(context,"User data doesn't exist",Toast.LENGTH_SHORT).show()
        }

        }

    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    //ChatScreen(roomId = "")
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatMessageItem(message: Message) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        horizontalAlignment = if(message.isSentByCurrentUser) Alignment.Start else Alignment.End) {

        Box(Modifier
            .background(
                color =
                    if (message.isSentByCurrentUser) colorResource(id = R.color.purple_700) else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
        ){
            Text(text=message.text,color=Color.White, style = TextStyle(fontSize =16.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(message.senderId,color=Color.Gray, style = TextStyle(fontSize =9.sp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(formatTimestamp(message.timestamp),color=Color.Gray,
                style = TextStyle(fontSize =5.sp))

        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun formatTimestamp(timestamp: Long): String {
    val messageDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val now = LocalDateTime.now()

    return when {
        isSameDay(messageDateTime, now) -> "today ${formatTime(messageDateTime)}"
        isSameDay(messageDateTime.plusDays(1), now) -> "yesterday ${formatTime(messageDateTime)}"
        else -> formatDate(messageDateTime)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
private fun isSameDay(dateTime1 : LocalDateTime, dateTime2 : LocalDateTime): Boolean{
    val formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime1.format(formatter)==dateTime2.format(formatter)//it is used to check
// whether the message is received today or not
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatTime(dateTime : LocalDateTime): String{
    val formatter= DateTimeFormatter.ofPattern("HH-MM")
    return formatter.format(dateTime)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateTime : LocalDateTime): String{
    val formatter= DateTimeFormatter.ofPattern("MMM d, yyyy")
    return formatter.format(dateTime)
}
