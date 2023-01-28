package com.example.todolist.data.repositories

import com.example.todolist.data.network.DataStoreManager
import com.example.todolist.data.network.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val dataStore: DataStoreManager,
    private val oneTapClient: SignInClient,
) {

    var password = ""
    var authGoogle = false
    var authEmail = false

    suspend fun signUpUser(email: String, password: String) {
        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password).await()
            addUserToFirestore()
            dataStore.savetoDataStore(password, authGoogle = false, authEmail = true)
        }
    }

    suspend fun signInUser(email: String, password: String) {
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password).await()
            dataStore.savetoDataStore(password, authGoogle = false, authEmail = true)
        }
    }

    suspend fun signInWithGoogle(account: GoogleSignInAccount) {
        withContext(Dispatchers.IO) {
            val task =
                auth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))
                    .await()

            val isNewUser = task.additionalUserInfo?.isNewUser
            if (isNewUser == true) {
                addUserToFirestore()
            }
            dataStore.savetoDataStore(password = "", authGoogle = true, authEmail = false)
        }
    }

    private suspend fun addUserToFirestore() {
        auth.currentUser?.apply {
            val user = User(uid, email)
            db.collection(USERS).document(user.userId).set(user).await()
        }
    }

    suspend fun getUser() = withContext(Dispatchers.IO) {
        auth.currentUser
    }

    suspend fun oneTapClientSignOut() {
        withContext(Dispatchers.IO) {
            oneTapClient.signOut().await()
        }
    }

    suspend fun firebaseAuthSignOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
            dataStore.savetoDataStore(password = "", authGoogle = false, authEmail = false)
        }
    }

    suspend fun deleteAccount() {
        dataStore.authPreference.first {
            password = it.password
            authEmail = it.authEmail
            authGoogle = it.authGoogle
            true
        }

        withContext(Dispatchers.IO) {
            val currentUser = getUser()!!
            val userEmail = currentUser.email!!
            val userId = currentUser.uid

            if (authGoogle) {
                oneTapClientSignOut()
            } else if (authEmail) {
                val credential = EmailAuthProvider
                    .getCredential(userEmail, password)
                currentUser.reauthenticate(credential).await()
            }

            currentUser.delete().await()
            db.collection(TODOS).whereEqualTo("createdBy.userId", userId).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val documents = it.result.documents
                        for (document in documents) {
                            db.collection(TODOS).document(document.id).delete()
                        }
                    }
                }
            db.collection(USERS).document(userId).delete().await()
            firebaseAuthSignOut()
        }
    }
}