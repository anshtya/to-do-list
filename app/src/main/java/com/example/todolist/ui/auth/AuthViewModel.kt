package com.example.todolist.ui.auth

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.model.Response
import com.example.todolist.data.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val userAuthenticatedStatus get() = authRepository.userAuthenticatedStatus

    private val _userAuthorizedEmail = MutableSharedFlow<Response<Boolean>>()
    val userAuthorizedEmail = _userAuthorizedEmail.asSharedFlow()

    private val _userAuthorizedGoogle = MutableSharedFlow<Response<Boolean>>()
    val userAuthorizedGoogle = _userAuthorizedGoogle.asSharedFlow()

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorizedEmail.emit(Response.Loading)
        _userAuthorizedEmail.emit(authRepository.signUpUser(email, password))
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorizedEmail.emit(Response.Loading)
        _userAuthorizedEmail.emit(authRepository.signInUser(email, password))
    }

    fun signInWithGoogle(result: ActivityResult) = viewModelScope.launch {
        _userAuthorizedGoogle.emit(Response.Loading)
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)
            _userAuthorizedGoogle.emit(authRepository.signInWithGoogle(account))
        } catch (e: Exception) {
            _userAuthorizedGoogle.emit(Response.Error(e))
        }
    }
}