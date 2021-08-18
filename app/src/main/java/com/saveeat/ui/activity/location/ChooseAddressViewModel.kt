package com.saveeat.ui.activity.location

import android.content.Context
import android.location.Address
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.auth.signup.SignupModel
import com.saveeat.model.response.Google.place.GooglePlacesBean
import com.saveeat.model.response.SaveEat.auth.AuthModel
import com.saveeat.model.response.SaveEat.location.LocationModel
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseAddressViewModel @Inject constructor(val repository: ChooseAddressRepository) : ViewModel() {

    private val _addressResposne: MutableLiveData<Resource<List<Address>?>?> = MutableLiveData()
    val addressResposne: LiveData<Resource<List<Address>?>?> get() = _addressResposne


    private val _placeDetail: MutableLiveData<Resource<GooglePlacesBean?>> = MutableLiveData()
    val placeDetail: LiveData<Resource<GooglePlacesBean?>> get() = _placeDetail


    private val _userSignup: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val userSignup: LiveData<Resource<AuthModel?>> get() = _userSignup

    private val _updateAddress: MutableLiveData<Resource<AuthModel?>> = MutableLiveData()
    val updateAddress: LiveData<Resource<AuthModel?>> get() = _updateAddress


    fun getCurrentAddres(context: Context?, latitute: Double?, longitute: Double?, handler: Handler?) {
        viewModelScope.launch {
            _addressResposne.value = Resource.Loading
            _addressResposne.value = repository.getCurrentAddres(context = context, latitute = latitute, longitute = longitute, handler = handler)
        }
    }

    fun getLatLongFromPlaceId(placeid: String?) {
        viewModelScope.launch {
            _placeDetail.value = Resource.Loading
            _placeDetail.value = repository.getPlaceDetail(placeid = placeid)
        }
    }

    fun signUp(data: SignupModel?) {
        viewModelScope.launch {
            _userSignup.value = Resource.Loading
            _userSignup.value = repository.signUp(data = data)
        }

    }

    fun updateAddress(data: LocationModel?,token: String?) {
        viewModelScope.launch {
            _updateAddress.value = Resource.Loading
            _updateAddress.value = repository.updateAddress(data = data,token=token)
        }

    }
}





