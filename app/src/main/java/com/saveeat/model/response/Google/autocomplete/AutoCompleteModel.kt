package com.saveeat.model.response.Google.autocomplete


data class AutoCompleteModel(
    val predictions: List<Prediction>,
    val status: String
)