package com.example.todolist.data.network

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
) {

    fun signUpUser(email: String, password: String) = firebaseAuth.createUserWithEmailAndPassword(email, password)

    fun signInUser(email: String, password: String) = firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signInWithGoogle(account: GoogleSignInAccount) = firebaseAuth.signInWithCredential(
        GoogleAuthProvider.getCredential(
            account.idToken, null
        )
    )

    fun signOutUser() {
        oneTapClient.signOut()
        firebaseAuth.signOut()
    }

    fun getUser() = firebaseAuth.currentUser

}