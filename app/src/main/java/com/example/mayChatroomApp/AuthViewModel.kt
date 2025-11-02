package com.example.mayChatroomApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mayChatroomApp.data.UserRepository
import com.example.mayChatroomApp.data.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.Boolean

class AuthViewModel: ViewModel(){
    private val userRepository : UserRepository

    init {
        userRepository= UserRepository(
            FirebaseAuth.getInstance(),
            Injection.instance()

        )
    }
    private val _authResult= MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>>get()  =_authResult
    /*Use mutableLiveData when your state needs to survive configuration changes or be shared
    across screens.

Use remember { mutableStateOf() } for transient UI state that doesn’t need to persist or be shared.
*/
    fun signUp(email:String,password: String,firstName: String,lastName: String){
        viewModelScope.launch {
            _authResult.value= userRepository.signUp(email,password,firstName,lastName)
        }
    }
    fun logIn(email:String,password: String){
        viewModelScope.launch {
            _authResult.value= userRepository.login(email,password)
        }
    }
}