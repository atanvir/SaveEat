package com.saveeat.ui.activity.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.auth.forgot.ForgotModel
import com.saveeat.model.request.auth.login.LoginModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel  @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _userLogin: MutableLiveData<Resource<AuthModel?>?> = MutableLiveData()
    val userLogin: LiveData<Resource<AuthModel?>?> get() = _userLogin

    private val _checkMobilForForgotPassword: MutableLiveData<Resource<AuthModel?>?> = MutableLiveData()
    val checkMobilForForgotPassword: LiveData<Resource<AuthModel?>?> get() = _checkMobilForForgotPassword


    fun forgot(model : Any?){
        viewModelScope.launch {
            _checkMobilForForgotPassword.value=repository.forgot(model)
        }
    }

    fun login(model : LoginModel?){
        viewModelScope.launch {
            _userLogin.value=repository.login(model)
        }
    }
}