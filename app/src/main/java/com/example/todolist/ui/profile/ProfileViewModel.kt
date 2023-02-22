package com.example.todolist.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.network.User
import com.example.todolist.domain.ProfileUseCase
import com.example.todolist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
): ViewModel() {

    private val _userProfile = MutableSharedFlow<Resource>()
    val userProfile: SharedFlow<Resource>
        get() = _userProfile

    private val _userProfileDetails = MutableStateFlow<Resource>(Resource.Success(null))
    val userProfileDetails: StateFlow<Resource>
        get() = _userProfileDetails

    fun getUser() = viewModelScope.launch {
        val currentUser = profileUseCase.getUser()
        _userProfileDetails.value = Resource.Success(User(currentUser!!.uid, currentUser.email))
    }

    fun signOutUser() = viewModelScope.launch {
        _userProfile.emit(Resource.Loading())
        try {
            profileUseCase.oneTapClientSignOut()
            profileUseCase.firebaseAuthSignOut()
            _userProfile.emit(Resource.Success())
        } catch (e: Exception){
            _userProfile.emit(Resource.Error(e.message))
        }
    }

    fun deleteUser() = viewModelScope.launch{
        _userProfile.emit(Resource.Loading())
        try {
            val currentUser = profileUseCase.getUser()
            currentUser?.let {
                profileUseCase.deleteAccount()
            }
            _userProfile.emit(Resource.Success())
        } catch (e: Exception){
            _userProfile.emit(Resource.Error(e.message))
        }
    }
}