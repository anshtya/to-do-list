package com.example.todolist.data.repositories

import com.example.todolist.data.network.FirebaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseSource: FirebaseSource
) {

    fun signUpUser(email: String, password: String) = firebaseSource.signUpUser(email, password)

    fun signInUser(email: String, password: String) = firebaseSource.signInUser(email, password)

    fun signInWithGoogle(account: GoogleSignInAccount) = firebaseSource.signInWithGoogle(account)

    fun signOutUser() = firebaseSource.signOutUser()

    fun getUser() = firebaseSource.getUser()
}