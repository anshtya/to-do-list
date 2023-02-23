package com.example.todolist.data.repositories

import com.example.todolist.data.network.datastore.DataStoreManager
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.network.model.User
import com.example.todolist.util.Constants.Companion.USERS
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val dataStore: DataStoreManager,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val userAuthenticatedStatus = auth.currentUser != null
    suspend fun signUpUser(email: String, password: String) =
        withContext(defaultDispatcher) {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                addUserToFirestore()
                dataStore.savetoDataStore(password, authGoogle = false, authEmail = true)
                Response.Success(true)
            } catch (e: Exception) {
                Response.Error(e)
            }
        }

    suspend fun signInUser(email: String, password: String) =
        withContext(defaultDispatcher) {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                dataStore.savetoDataStore(password, authGoogle = false, authEmail = true)
                Response.Success(true)
            } catch (e: Exception) {
                Response.Error(e)
            }
        }

    suspend fun signInWithGoogle(account: GoogleSignInAccount) =
        withContext(defaultDispatcher) {
            try {
                val task = auth.signInWithCredential(
                    GoogleAuthProvider.getCredential(account.idToken, null)
                ).await()
                val isNewUser = task.additionalUserInfo?.isNewUser
                if (isNewUser == true) {
                    addUserToFirestore()
                }
                dataStore.savetoDataStore(password = "", authGoogle = true, authEmail = false)
                Response.Success(true)
            } catch (e: Exception) {
                Response.Error(e)
            }
        }

    private suspend fun addUserToFirestore() {
        withContext(defaultDispatcher) {
            auth.currentUser?.apply {
                val user = User(uid, email)
                db.collection(USERS).document(user.userId).set(user).await()
            }
        }
    }
}