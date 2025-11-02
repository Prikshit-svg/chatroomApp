package com.example.mayChatroomApp.data



sealed class Result<out T>{
    data class Success<out T>(val data: T):Result<T>()
    data class Error<out Nothing>(val error : Exception): Result<Nothing>()//Nothing signifies that
// "there is no value of type T here."as we know Error doesn't carry a successful value of type T.
    //so it is done so that we can avoid error by passing Nothing as it's return/output type
}
/*: Result<Nothing>():
    •This indicates that Error is a subtype of Result.•Nothing: This is a special type in Kotlin.
    •It has no instances. You cannot create an object of type Nothing.
    •It is a subtype of all other types.

 */