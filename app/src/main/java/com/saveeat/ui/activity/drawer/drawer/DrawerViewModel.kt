package com.saveeat.ui.activity.drawer.drawer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.model.response.saveeat.badge.BadgeModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrawerViewModel @Inject constructor(private val repository: DrawerRepository) : ViewModel() {

    private val _userLogout : MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val userLogout : LiveData<Resource<AuthModel?>>  get()=_userLogout


    private val _userBadgesEarning : MutableLiveData<Resource<BadgeModel?>> = MutableLiveData()
    val userBadgesEarning : LiveData<Resource<BadgeModel?>>  get()=_userBadgesEarning

    fun userLogout(token : String?){
        viewModelScope.launch {
            _userLogout.value=repository.userLogout(token)
        }
    }

    fun userBadgesEarning(model: com.saveeat.model.request.badge.BadgeModel?){
        viewModelScope.launch {
            _userBadgesEarning.value=repository.userBadgesEarning(model)
        }
    }



}