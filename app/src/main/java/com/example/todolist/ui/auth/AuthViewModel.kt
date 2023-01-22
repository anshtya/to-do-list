package com.example.todolist.ui.auth

import androidx.lifecycle.ViewModel
import com.example.todolist.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){

    val userAuthorized = authRepository.userAuthorized

    fun signInUser(email: String, password: String) = authRepository.signInUser(email, password)

    fun signUpUser(email: String, password: String) = authRepository.signUpUser(email, password)

    fun signOutUser() = authRepository.signOutUser()

}