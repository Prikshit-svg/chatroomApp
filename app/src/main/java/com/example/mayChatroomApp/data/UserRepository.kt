package com.example.mayChatroomApp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth, private val firestore: FirebaseFirestore
){
    suspend fun signUp(email: String, password: String, firstName: String, lastName: String): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user : User){
        firestore.collection("user").document(user.email).set(user).await()
        //firestore saves data as documents within a collection. For each user document we will save
    // them in a collection called users providing their email as a key rather than letting firestore
    // generate random characters. With this email we can then manage each user better in the chatroom.
    }

    suspend fun login(email: String, password: String): Result<Boolean> =try{
        auth.signInWithEmailAndPassword(email, password).await()
        //When you call .await() on a Firebase Task, it suspends the current coroutine until the Task
        // completes (either successfully or with an error).
        // •If the Firebase Task completes successfully, .await() returns the result of the task
        // (in this case, an AuthResult object, though you are not directly using its contents here,
        // just the fact that it succeeded).•If the Firebase Task fails, .await() will throw an
        // exception (e.g., FirebaseAuthInvalidUserException, FirebaseAuthInvalidCredentialsException,
        // etc.), which will then be caught by your catch block.
        Result.Success(true)
    }catch(e: Exception){
        Result.Error(e)
    }

    suspend fun getCurrentUser(): Result<User> =try{
        val uid=auth.currentUser?.email
        if (uid!=null){
            val userDocument=firestore.collection("users").document(uid).get().await()
            val user=userDocument.toObject(User::class.java)
            if (user!=null){
                Result.Success(user)
            }
            else{
                Result.Error(Exception("User data not found"))
            }
        }
        else{
            Result.Error(Exception("User not signed up"))
        }
    }catch (e:Exception){
        Result.Error(e)
    }
}