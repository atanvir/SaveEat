package com.saveeat.ui.activity.auth.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordViewModel  @Inject constructor(private val repository: PasswordRepository) : ViewModel() {

    private val _forgotPassword: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val forgotPassword: LiveData<Resource<AuthModel?>> get() = _forgotPassword


    fun forgotPassword(model : ForgotModel?){
        viewModelScope.launch {
            _forgotPassword.value=repository.forgotPassword(model)
        }
    }
}