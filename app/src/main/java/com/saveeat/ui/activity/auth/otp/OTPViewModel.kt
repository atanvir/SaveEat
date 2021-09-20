package com.saveeat.ui.activity.auth.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.request.auth.login.LoginOTPModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.ui.activity.auth.login.LoginRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OTPViewModel @Inject constructor(private val repository: OTPRepository) : ViewModel() {

    private val _loginByOtp: MutableLiveData<Resource<AuthModel?>?> = MutableLiveData()
    val loginByOtp: LiveData<Resource<AuthModel?>?> get() = _loginByOtp


    fun login(model : LoginOTPModel?){
        viewModelScope.launch {
            _loginByOtp.value=repository.loginByOtp(model)
        }
    }


}