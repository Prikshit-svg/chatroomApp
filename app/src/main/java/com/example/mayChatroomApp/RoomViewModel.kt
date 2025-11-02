package com.example.mayChatroomApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mayChatroomApp.data.Result
import com.example.mayChatroomApp.data.Room
import com.example.mayChatroomApp.data.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel: ViewModel() {
    private val _rooms = MutableLiveData<List<Room>>()
    val rooms : LiveData<List<Room>> get() = _rooms
    private val roomRepository : RoomRepository

    init {
        roomRepository = RoomRepository(Injection.instance())
        loadRooms()
    }

    fun createRoom(name : String) {
        viewModelScope.launch {
            roomRepository.createRoom(name)
        }

    }

      // In RoomViewModel.kt

    fun loadRooms() {
        viewModelScope.launch {
            // It collects the flow from the repository
            roomRepository.getRooms().collect { result ->
                // Check if the result is a success
                if (result is Result.Success) {
                    // --- THIS IS THE FIX ---
                    // Directly assign the data from the successful result to the LiveData.
                    // DO NOT process or re-map the list. Trust the repository.
                    _rooms.value = result.data
                }
                // Optionally handle the error case
                // if (result is Result.Error) { ... log the error ... }
            }
        }
    }

}
