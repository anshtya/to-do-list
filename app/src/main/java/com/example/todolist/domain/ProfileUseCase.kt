package com.example.todolist.domain

import com.example.todolist.data.repositories.AuthRepository
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun getUser() = authRepository.getUser()

    suspend fun oneTapClientSignOut() = authRepository.oneTapClientSignOut()

    suspend fun firebaseAuthSignOut() = authRepository.firebaseAuthSignOut()

    suspend fun deleteAccount() = authRepository.deleteAccount()
}