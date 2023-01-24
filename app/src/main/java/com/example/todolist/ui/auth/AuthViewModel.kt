package com.example.todolist.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repositories.AuthRepository
import com.example.todolist.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userAuthorized = MutableStateFlow<Resource<FirebaseUser>>(Resource.Loading())
    val userAuthorized: StateFlow<Resource<FirebaseUser>>
        get() = _userAuthorized

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorized.value = Resource.Loading()
        try {
            val task = authRepository.signUpUser(email, password).await()
            _userAuthorized.value = Resource.Success(task.user)
        } catch (e: Exception) {
            _userAuthorized.value = Resource.Error(e.message)
        }
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorized.value = Resource.Loading()
        try {
            val task = authRepository.signInUser(email, password).await()
            _userAuthorized.value = Resource.Success(task.user)
        } catch (e: Exception) {
            _userAuthorized.value = Resource.Error(e.message)
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
        _userAuthorized.value = Resource.Loading()
        try{
            val task = authRepository.signInWithGoogle(account).await()
            _userAuthorized.value = Resource.Success(task.user)
        } catch (e: Exception) {
            _userAuthorized.value = Resource.Error(e.message)
        }
    }

    fun signOutUser() = authRepository.signOutUser()

    fun getUser() = authRepository.getUser()

}