package com.example.todolist.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.repositories.AuthRepository
import com.example.todolist.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userAuthorized = MutableSharedFlow<Resource>()
    val userAuthorized: SharedFlow<Resource>
        get() = _userAuthorized

    private val _userAuthorizedGoogle = MutableSharedFlow<Resource>()
    val userAuthorizedGoogle: SharedFlow<Resource>
        get() = _userAuthorizedGoogle

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        try {
            authRepository.signUpUser(email, password)
            _userAuthorized.emit(Resource.Success())
        } catch (e: Exception) {
            _userAuthorized.emit(Resource.Error(e.message))
        }
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        try {
            authRepository.signInUser(email, password)
            _userAuthorized.emit(Resource.Success())
        } catch (e: Exception) {
            _userAuthorized.emit(Resource.Error(e.message))
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount) = viewModelScope.launch {
        try{
            authRepository.signInWithGoogle(account)
            _userAuthorizedGoogle.emit(Resource.Success())
        } catch (e: Exception) {
            _userAuthorizedGoogle.emit(Resource.Error(e.message))
        }
    }

    fun signInWithGoogleCancelled() = viewModelScope.launch {
        _userAuthorizedGoogle.emit(Resource.Error(null))
    }
}