package com.example.todolist.data.repositories

import com.example.todolist.data.network.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) {

    suspend fun signUpUser(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseSource.signUpUser(email, password)
    }

    suspend fun signInUser(email: String, password: String) = withContext(Dispatchers.IO) {
        firebaseSource.signInUser(email, password)
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount) = withContext(Dispatchers.IO) {
        firebaseSource.signInWithGoogle(account)
    }

    fun signOutUser() = firebaseSource.signOutUser()

    fun getUser() = firebaseSource.getUser()
}