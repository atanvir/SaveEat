package com.saveeat.ui.activity.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel  @Inject constructor(private val repository: SignupRepository) : ViewModel() {

    private val _checkUserMobileAndEmail: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val checkUserMobileAndEmail: LiveData<Resource<AuthModel?>> get() = _checkUserMobileAndEmail


    fun checkUserMobileAndEmail(email : String?,mobileNumber: String?){
        viewModelScope.launch {
            _checkUserMobileAndEmail.value=repository.checkUserMobileAndEmail(email,mobileNumber)
        }
    }
}