package com.example.todolist.data.repositories

import com.example.todolist.data.network.UserProfileSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val userProfileSource: UserProfileSource
) {
    suspend fun getUser() = withContext(Dispatchers.IO) {
        userProfileSource.getUser()
    }

    suspend fun oneTapClientSignOut() = withContext(Dispatchers.IO){
        userProfileSource.oneTapClientSignOut()
    }

    suspend fun firebaseAuthSignOut() = withContext(Dispatchers.IO){
        userProfileSource.firebaseAuthSignOut()
    }

    suspend fun deleteAccount() = withContext(Dispatchers.IO){
        userProfileSource.disableAccount()
    }

    suspend fun revokeAccess() = withContext(Dispatchers.IO){
        userProfileSource.revokeAccess()
    }
}