package com.saveeat.ui.activity.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saveeat.model.request.rating.RatingModel
import com.saveeat.model.response.saveeat.auth.AuthModel
import com.saveeat.ui.activity.profile.ProfileRepository
import com.saveeat.utils.application.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RatingViewModel @Inject constructor(private val repository: RatingRepository) : ViewModel() {

    private val _ratingByUser: MutableLiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel?>?> = MutableLiveData()
    val ratingByUser: LiveData<Resource<com.saveeat.model.response.saveeat.order.OrderHistoryModel?>?> get() = _ratingByUser

    fun ratingByUser(model : RatingModel?){
        viewModelScope.launch {
            _ratingByUser.value=repository.ratingByUser(model)
        }
    }

}