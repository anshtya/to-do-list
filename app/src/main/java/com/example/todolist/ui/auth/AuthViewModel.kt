package com.example.todolist.ui.auth

import androidx.lifecycle.ViewModel
import com.example.todolist.data.repositories.AuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userAuthorized = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())

    init {
        if (getUser() != null){
            userAuthorized.value = Resource.Success(getUser())
        }
    }

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
                    userAuthorized.value = Resource.Success(getUser()!!)
                } else {
                    userAuthorized.value = Resource.Error(task.exception?.message)
                }
            }
    }

    fun signOutUser() = authRepository.signOutUser()

    fun getUser() = authRepository.getUser()

}

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)

    class Error<T>(message: String?, data: T? = null) : Resource<T>(data, message)

    class Loading<T> : Resource<T>()
}