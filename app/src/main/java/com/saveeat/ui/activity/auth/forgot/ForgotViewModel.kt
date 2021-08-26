package com.saveeat.ui.activity.auth.forgot

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
class ForgotViewModel  @Inject constructor(private val repository: ForgotRepository) : ViewModel() {

    private val _checkMobilForForgotPassword: MutableLiveData<Resource<AuthModel?>?> = MutableLiveData()
    val checkMobilForForgotPassword: LiveData<Resource<AuthModel?>?> get() = _checkMobilForForgotPassword


    fun forgot(model : ForgotModel?){
        viewModelScope.launch {
            _checkMobilForForgotPassword.value=repository.forgot(model)
        }
    }
}