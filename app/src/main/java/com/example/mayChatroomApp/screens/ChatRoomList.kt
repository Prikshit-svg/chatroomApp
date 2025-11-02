package com.example.mayChatroomApp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField


import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mayChatroomApp.RoomViewModel
import com.example.mayChatroomApp.data.Room
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomList(
    roomViewModel: RoomViewModel= viewModel(),
    onJoinClicked:(Room)-> Unit
){
    val showDialog = remember{mutableStateOf(false)}
    val name= remember{mutableStateOf("")}
    val roomList by roomViewModel.rooms.observeAsState(emptyList())
//(emptyList()): This is the initial value provided to observeAsState. *   When ChatRoomList first
// composes, and before the LiveData has emitted its first actual list of rooms (which might take a
// moment if it involves an asynchronous database call), the State object will hold this emptyList().
        Column(
            Modifier.padding(16.dp).fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(16.dp))
            Text("Chat rooms",fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(roomList){
                    room ->
                    RoomItem(room=room, { onJoinClicked(room) })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                showDialog.value=true

            },modifier = Modifier.fillMaxWidth()) {
                Text("Create a new room")

            }
            if(showDialog.value){
                AlertDialog(onDismissRequest = {
                    showDialog.value=true

                }, title ={Text("Create a new room")} ,
                    text = {OutlinedTextField(value=name.value, onValueChange = {name.value=it},
                        modifier = Modifier.padding(10.dp).fillMaxWidth(), singleLine = true)},
                    confirmButton = {
                        Row(Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Button(onClick = {
                                if (name.value.isNotBlank()){
                                    showDialog.value=false
                                    roomViewModel.createRoom(name.value)
                                }

                            }) {
                                Text("Add")
                            }
                            Button(onClick = {
                                showDialog.value=false
                            }) {
                                Text("Cancel")
                            }

                        }
                })


            }
        }
    }

@Preview(showBackground = true)
@Composable
fun ChatRoomListPreview(){
   // RoomItem(room = Room(id = "1", name = "Room1"))
}

@Composable
fun RoomItem(room: Room, onJoinClicked: (Room) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = room.name, fontWeight = FontWeight.Normal, fontSize = 16.sp)
        Text(text = room.id, fontStyle = FontStyle.Italic,fontSize = 12.sp)
        OutlinedButton(onClick = {
            // Check if room ID is valid before navigating
            if (room.id.isNotEmpty()) {
                onJoinClicked(room)
            } else {
                Log.e("RoomItem", "Room ID is empty")
            }
        }) {
            Text(text = "Join")
        }

    }
}