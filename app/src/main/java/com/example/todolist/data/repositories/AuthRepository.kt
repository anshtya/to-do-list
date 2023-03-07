package com.example.todolist.data.repositories

import com.example.todolist.data.network.datastore.DataStoreManager
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.network.model.User
import com.example.todolist.util.Constants.Companion.TODOS
import com.example.todolist.util.Constants.Companion.USERS
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val dataStore: DataStoreManager,
    private val oneTapClient: SignInClient,
    private val defaultDispatcher: CoroutineDispatcher
) {
    private var password = ""
    private var authGoogle = false
    private var authEmail = false
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
                    val credential = EmailAuthProvider.getCredential(userEmail, password)
                    currentUser.reauthenticate(credential).await()
                }

                currentUser.delete().await()
                val userTodos =
                    db.collection(TODOS).whereEqualTo("createdBy.userId", userId).get()
                        .await().documents
                for (todo in userTodos) {
                    db.collection(TODOS).document(todo.id).delete().await()
                }
                db.collection(USERS).document(userId).delete().await()
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