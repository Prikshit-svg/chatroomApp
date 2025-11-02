package com.example.mayChatroomApp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RoomRepository (private val firestore : FirebaseFirestore){
    private val roomsCollection = firestore.collection("rooms")
    suspend fun createRoom(name: String) : Result<Unit>{
        //•Result<Unit>:
        // This refers to your custom sealed class Result. It indicates that the function will return
        // an object representing either:•Result.Success(Unit): The room creation was successful.
        // Unit is used when there's no specific data to return upon success, just the confirmation
        // of success itself.

        // •Result.Error(exception): An error occurred during the room creation process.
        try {
            val room=Room(name = name)
            firestore.collection("rooms").add(room).await()
            return Result.Success(Unit)
        }catch (e: Exception){
            return Result.Error(e)

        }

    }
    fun getRooms(): Flow<Result<List<Room>>> = callbackFlow {
        val snapshotListener = roomsCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Result.Error(error)).isSuccess
                return@addSnapshotListener
            }

            if (snapshot != null) {
                // Manually map each document to a Room object
                val rooms = snapshot.documents.mapNotNull { doc ->
                    // Convert the document to a Room object. Firestore handles the 'name' field.
                    val room = doc.toObject<Room>()

                    // Manually set the id from the document's ID.
                    // This guarantees the ID is never empty.
                    room?.apply { id = doc.id }
                }
                trySend(Result.Success(rooms)).isSuccess
            }
        }
        awaitClose { snapshotListener.remove() }
    }
    // --- END OF REPLACEMENT ---
}
/*
•querySnapshot.documents: The QuerySnapshot object has a documents property, which is a
List<DocumentSnapshot>. Each DocumentSnapshot in this list represents a single document retrieved
from the "rooms" collection.

•.map { document -> ... }: This is a standard Kotlin collection extension function. It iterates over
each DocumentSnapshot (which it names document for each iteration) in the querySnapshot.documents
 list and transforms it into something else based on the code inside the lambda { ... }. The results
 of these transformations are collected into a new list, which is then assigned to the rooms variable.

 •Inside the map lambda: document.toObject(Room::class.java)!!.copy(id = document.id)

 •document.toObject(Room::class.java): This is a method on DocumentSnapshot. It attempts to automatically
 convert the data from the Firestore document into an instance of your Room data class.

 •For this to work, the fields in your Firestore document must match the property names in your
 Room data class (or you need to use @PropertyName annotations in your Room class if they differ).

 •It requires a no-argument constructor for your Room class (data classes get one by default, but if
 you have default values for all properties, ensure they allow Firestore to instantiate it).
 •This method returns a nullable Room? object because the conversion might fail (e.g., if the document
 data doesn't match the Room class structure).

 •!!: This is the Kotlin non-null asserted call. You are asserting here that
 document.toObject(Room::class.java) will never be null. This is a potential crash point. If a document
 in your Firestore collection cannot be converted to a Room object (e.g., it's malformed, or it's an
 older document with a different structure), this will throw a NullPointerException.
 */