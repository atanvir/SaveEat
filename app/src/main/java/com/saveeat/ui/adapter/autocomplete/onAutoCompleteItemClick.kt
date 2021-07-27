package com.biteMe.ui.adapter.autocomplete

interface onAutoCompleteItemClick {
    fun onClick(placeId:String?,spotName:String?)
    fun loader(visible : Boolean?)
}