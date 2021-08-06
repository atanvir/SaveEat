package com.saveeat.ui.activity.location

import android.content.Context
import android.location.Address
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.response.Google.place.GooglePlacesBean
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseAddressViewModel @Inject constructor(val repository: ChooseAddressRepository) :
    ViewModel() {

    private val _addressResposne: MutableLiveData<Resource<List<Address>?>?> = MutableLiveData()
    val addressResposne: LiveData<Resource<List<Address>?>?> get() = _addressResposne


    private val _placeDetail: MutableLiveData<Resource<GooglePlacesBean?>> = MutableLiveData()
    val placeDetail: LiveData<Resource<GooglePlacesBean?>> get() = _placeDetail

    fun getCurrentAddres(
        context: Context?,
        latitute: Double?,
        longitute: Double?,
        handler: Handler?
    ) {
        viewModelScope.launch {
            _addressResposne.value = Resource.Loading
            _addressResposne.value = repository.getCurrentAddres(
                context = context,
                latitute = latitute,
                longitute = longitute,
                handler = handler
            )
        }
    }

    fun getLatLongFromPlaceId(placeid: String?) {
        viewModelScope.launch {
            _placeDetail.value = Resource.Loading
            _placeDetail.value = repository.getPlaceDetail(placeid = placeid)
        }
    }
}





