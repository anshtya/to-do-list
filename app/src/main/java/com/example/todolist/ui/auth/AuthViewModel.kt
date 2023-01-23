package com.example.todolist.ui.auth

import androidx.lifecycle.ViewModel
import com.example.todolist.data.repositories.AuthRepository
import com.example.todolist.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userAuthorized = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val userAuthorizedGoogle = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())

    fun signUpUser(email: String, password: String) {
        userAuthorized.value = Resource.Loading()
        authRepository.signUpUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userAuthorized.value = Resource.Success(getUser())
                } else {
                    userAuthorized.value = Resource.Error(task.exception?.message)
                }
            }
    }

    fun signInUser(email: String, password: String) {
        userAuthorized.value = Resource.Loading()
        authRepository.signInUser(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userAuthorized.value = Resource.Success(getUser())
                } else {
                    userAuthorized.value = Resource.Error(task.exception?.message)
                }
            }
    }

    fun signInWithGoogle(account: GoogleSignInAccount){
        userAuthorizedGoogle.value = Resource.Loading()
        authRepository.signInWithGoogle(account)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userAuthorizedGoogle.value = Resource.Success(getUser())
                } else {
                    userAuthorizedGoogle.value = Resource.Error(task.exception?.message)
                }
            }
    }

    fun signOutUser() = authRepository.signOutUser()

    fun getUser() = authRepository.getUser()

}