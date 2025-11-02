package com.example.mayChatroomApp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mayChatroomApp.data.Message
import com.example.mayChatroomApp.data.MessageRepository
import com.example.mayChatroomApp.data.Result
import com.example.mayChatroomApp.data.User
import com.example.mayChatroomApp.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
val toastMessage = MutableLiveData<Boolean>(false)
class MessageViewModel : ViewModel() {

    private val messageRepository : MessageRepository
    private val userRepository: UserRepository
    init{
        messageRepository=MessageRepository(Injection.instance())
        userRepository=UserRepository(FirebaseAuth.getInstance(), Injection.instance())
        loadCurrentUser()
    }

    private val _currentUser= MutableLiveData<User?>()
    val currentUser: MutableLiveData<User?> get()=_currentUser
    private val _roomId= MutableLiveData<String>()
    private val _messages= MutableLiveData<List<Message>>()
    val messages: MutableLiveData<List<Message>> get() = _messages

    //loadCurrentUser to identify the user on every accounts during a chat.
    fun loadCurrentUser(){
        viewModelScope.launch {
            when(val user=userRepository.getCurrentUser()){
                is Result.Success -> _currentUser.value=user.data
                is Result.Error ->{
                    toastMessage.value=true
                }
            }

        }
    }

    //loadMessages to retrieve messages within a room.
    fun loadMessages(){
        if(_roomId.value!=null){
            viewModelScope.launch {
                messageRepository.getChatMessages(_roomId.value.toString()).collect {_messages.value=it}
            }
        }
    }
    /*
    •The code inside the curly braces { ... } is executed every time the Flow emits a new value.
    •it: Represents the value emitted by the Flow. In this case, because the Flow is a Flow<List<Message>>, it is a List<Message>.
    •_messages.value = it: This line takes the list of messages (it) received from the Flow and assigns it to the value of your _messages MutableLiveData.
    •Because your UI (Activity or Composable screen) is likely observing the public val messages, this assignment automatically triggers a UI update, displaying the new list of messages.
    */


    fun setRoomId(roomId: String) {
        // Check if the ID is already set, to avoid loading messages twice
        if (_roomId.value == roomId) {
            return
        }
        _roomId.value = roomId // Store the new room ID
        loadMessages()         // NOW you can safely load the messages
    }

fun sendMessage(text: String){
    if(_currentUser!=null){
        val message=Message(
            senderId = _currentUser.value!!.email,
            senderName = _currentUser.value!!.firstName,
            text=text
        )
        viewModelScope.launch {
            when(messageRepository.sendMessage(_roomId.value!!.toString(),message)){
                is Result.Success-> Unit
                is Result.Error-> {}

            }
        }
    }
}


}
