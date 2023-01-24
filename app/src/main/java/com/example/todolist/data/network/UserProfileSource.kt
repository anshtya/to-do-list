package com.example.todolist.data.network

import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserProfileSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val oneTapClient: SignInClient,
    private val googleSignInClient: GoogleSignInClient
) {
    fun getUser() = firebaseAuth.currentUser

    fun oneTapClientSignOut() = oneTapClient.signOut()

    fun firebaseAuthSignOut() = firebaseAuth.signOut()

    fun disableAccount() = getUser()?.delete()

    fun revokeAccess() = googleSignInClient.revokeAccess()
}