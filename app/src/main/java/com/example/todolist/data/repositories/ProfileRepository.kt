package com.example.todolist.data.repositories

import com.example.todolist.data.network.datastore.DataStoreManager
import com.example.todolist.data.network.model.Response
import com.example.todolist.util.Constants
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val dataStore: DataStoreManager,
    private val oneTapClient: SignInClient,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private var password = ""
    private var authGoogle = false
    private var authEmail = false

    suspend fun signOutUser() =
        withContext(defaultDispatcher) {
            try {
                oneTapClient.signOut().await()
                firebaseAuthSignOut()
                Response.Success(true)
            } catch (e: Exception) {
                Response.Error(e)
            }
        }

    suspend fun deleteAccount() =
        withContext(defaultDispatcher) {
            try {
                dataStore.authPreference.first {
                    password = it.password
                    authEmail = it.authEmail
                    authGoogle = it.authGoogle
                    true
                }
                val currentUser = getUser()!!
                val userEmail = currentUser.email!!
                val userId = currentUser.uid

                if (authGoogle) {
                    oneTapClient.signOut().await()
                } else if (authEmail) {
                    val credential = EmailAuthProvider
                        .getCredential(userEmail, password)
                    currentUser.reauthenticate(credential).await()
                }

                currentUser.delete().await()
                db.collection(Constants.TODOS).whereEqualTo("createdBy.userId", userId).get()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val documents = it.result.documents
                            for (document in documents) {
                                db.collection(Constants.TODOS).document(document.id).delete()
                            }
                        }
                    }
                db.collection(Constants.USERS).document(userId).delete().await()
                firebaseAuthSignOut()
                Response.Success(true)
            } catch (e: Exception) {
                Response.Error(e)
            }
        }

    suspend fun getUser() = withContext(defaultDispatcher) {
        auth.currentUser
    }

    private suspend fun firebaseAuthSignOut() = withContext(defaultDispatcher) {
        auth.signOut()
        dataStore.savetoDataStore(password = "", authGoogle = false, authEmail = false)
    }
}