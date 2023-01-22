package com.example.todolist.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
    ) {

    private val currentUser = firebaseAuth.currentUser
    val userAuthorized = MutableStateFlow(currentUser)

    fun signUpUser(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    Log.d("account", "Account Created")
                    val user = firebaseAuth.currentUser
                    userAuthorized.value = user
                } else {
                    Log.w("account", "Account Not Created")
                }
            }
    }

    fun signInUser(email: String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Log.d("account", "Account Sign In")
                    val user = firebaseAuth.currentUser
                    userAuthorized.value = user
                } else {
                    Log.w("account", "Account Not Sign In")
                }
            }
    }

    fun signOutUser() = firebaseAuth.signOut()

}