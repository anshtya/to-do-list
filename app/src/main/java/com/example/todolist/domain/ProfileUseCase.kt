package com.example.todolist.domain

import com.example.todolist.data.repositories.ProfileRepository
import javax.inject.Inject

class ProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend fun getUser() = profileRepository.getUser()

    suspend fun deleteAccount() = profileRepository.deleteAccount()

    suspend fun signOutUser() = profileRepository.signOutUser()
}