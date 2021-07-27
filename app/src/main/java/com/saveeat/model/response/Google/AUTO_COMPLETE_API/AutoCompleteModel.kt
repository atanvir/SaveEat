package com.saveeat.model.response.Google.AUTO_COMPLETE_API


data class AutoCompleteModel(
    val predictions: List<Prediction>,
    val status: String
)