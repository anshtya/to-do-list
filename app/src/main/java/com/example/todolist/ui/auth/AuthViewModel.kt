package com.example.todolist.ui.auth

import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.AuthUseCase
import com.example.todolist.data.network.model.AuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _userAuthorized = MutableSharedFlow<AuthResult>()
    val userAuthorized = _userAuthorized.asSharedFlow()

    private val _userAuthorizedGoogle = MutableSharedFlow<AuthResult>()
    val userAuthorizedGoogle = _userAuthorizedGoogle.asSharedFlow()

    fun signUpUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorized.emit(AuthResult.Loading)
        _userAuthorized.emit(authUseCase.signUpUser(email, password))
    }

    fun signInUser(email: String, password: String) = viewModelScope.launch {
        _userAuthorized.emit(AuthResult.Loading)
        _userAuthorized.emit(authUseCase.signInUser(email, password))
    }

    fun signInWithGoogle(result: ActivityResult) = viewModelScope.launch {
        _userAuthorizedGoogle.emit(AuthResult.Loading)
        _userAuthorizedGoogle.emit(authUseCase.signInWithGoogle(result))
    }
}