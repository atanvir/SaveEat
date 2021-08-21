package com.saveeat.ui.activity.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.model.request.profile.ProfileModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(private val repository: ProfileRepository) : ViewModel() {

    private val _changeMobileNumber: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val changeMobileNumber: LiveData<Resource<AuthModel?>> get() = _changeMobileNumber


    private val _userUpdateDetails: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val userUpdateDetails: LiveData<Resource<AuthModel?>> get() = _userUpdateDetails


    fun changeMobileNumber(model : ProfileModel?){
        viewModelScope.launch {
            _changeMobileNumber.value=repository.changeMobileNumber(model)
        }
    }

    fun userUpdateDetails(model : ProfileModel?){
        viewModelScope.launch {
            _userUpdateDetails.value=repository.userUpdateDetails(model)
        }
    }
}