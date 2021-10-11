package com.saveeat.ui.activity.drawer.credit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.response.saveeat.main.filter.FilterCuisinesModel
import com.saveeat.model.response.saveeat.wallet.WalletModel
import com.saveeat.ui.activity.filter.FilterRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditViewModel  @Inject constructor(private val repository: CreditRepository) : ViewModel(){

    private val _getUserDetails : MutableLiveData<Resource<WalletModel?>?> = MutableLiveData()
    val getUserDetails : LiveData<Resource<WalletModel?>?> get() = _getUserDetails



    fun getUserDetail(token: String) {
        viewModelScope.launch {
            _getUserDetails.value=repository.getUserDetail(token)
        }

    }

}