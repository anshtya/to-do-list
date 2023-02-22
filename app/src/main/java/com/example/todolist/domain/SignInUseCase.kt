package com.example.todolist.domain

import com.example.todolist.data.repositories.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun signUpUser(email: String, password: String) =
        authRepository.signUpUser(email, password)

    suspend fun signInUser(email: String, password: String) =
        authRepository.signInUser(email, password)

    suspend fun signInWithGoogle(account: GoogleSignInAccount) =
        authRepository.signInWithGoogle(account)
}