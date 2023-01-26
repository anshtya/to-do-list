package com.example.todolist.data.repositories

import com.example.todolist.data.network.User
import com.example.todolist.util.Constants.Companion.USERS
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {

    suspend fun signUpUser(email: String, password: String) = withContext(Dispatchers.IO) {
        auth.createUserWithEmailAndPassword(email, password).await()
        addUserToFirestore()
    }

    suspend fun signInUser(email: String, password: String): Unit = withContext(Dispatchers.IO) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount) = withContext(Dispatchers.IO) {
        val task =
            auth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
                .await()

        val isNewUser = task.additionalUserInfo?.isNewUser
        if(isNewUser == true) {
            addUserToFirestore()
        }
    }

    private fun addUserToFirestore() = auth.currentUser?.apply {
        val user = User(uid, email)
        db.collection(USERS).document(user.userId).set(user)
    }
}