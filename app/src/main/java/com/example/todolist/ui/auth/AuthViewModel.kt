package com.example.todolist.ui.auth

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.AuthUseCase
import com.example.todolist.data.network.model.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    val userAuthenticatedStatus get() =  authUseCase.userAuthenticatedStatus

    private val _userAuthorizedEmail = MutableSharedFlow<Response<Boolean>>()
    val userAuthorizedEmail = _userAuthorizedEmail.asSharedFlow()

    private val _userAuthorizedGoogle = MutableSharedFlow<Response<Boolean>>()
    val userAuthorizedGoogle = _userAuthorizedGoogle.asSharedFlow()

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorizedEmail.emit(Response.Loading)
        _userAuthorizedEmail.emit(authUseCase.signUpUser(email, password))
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorizedEmail.emit(Response.Loading)
        _userAuthorizedEmail.emit(authUseCase.signInUser(email, password))
    }

    fun signInWithGoogle(result: ActivityResult) = viewModelScope.launch {
        _userAuthorizedGoogle.emit(Response.Loading)
        _userAuthorizedGoogle.emit(authUseCase.signInWithGoogle(result))
    }
}