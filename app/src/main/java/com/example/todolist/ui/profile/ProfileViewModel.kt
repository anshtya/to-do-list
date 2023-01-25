package com.example.todolist.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.User
import com.example.todolist.data.repositories.ProfileRepository
import com.example.todolist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProfileRepository: ProfileRepository
): ViewModel() {

    private val _userProfile = MutableSharedFlow<Resource>()
    val userProfile: SharedFlow<Resource>
        get() = _userProfile

    private val _userProfileDetails = MutableStateFlow<Resource>(Resource.Success(null))
    val userProfileDetails: StateFlow<Resource>
        get() = _userProfileDetails

    fun getUser() = viewModelScope.launch {
        val currentUser = userProfileRepository.getUser()
        _userProfileDetails.value = Resource.Success(User(currentUser?.email))
    }

    fun signOutUser() = viewModelScope.launch {
        try {
            userProfileRepository.oneTapClientSignOut().await()
            userProfileRepository.firebaseAuthSignOut()
            _userProfile.emit(Resource.Success())
        } catch (e: Exception){
            _userProfile.emit(Resource.Error(e.message))
        }
    }

    fun deleteUser() = viewModelScope.launch{
        try {
            val currentUser = userProfileRepository.getUser()
            currentUser?.let {
                userProfileRepository.apply {
                    revokeAccess().await()
                    oneTapClientSignOut().await()
                    deleteAccount()?.await()
                }
            }
            _userProfile.emit(Resource.Success())
        } catch (e: Exception){
            _userProfile.emit(Resource.Error(e.message))
        }
    }
}