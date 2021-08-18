package com.saveeat.model.response.SaveEat.auth

data class Location(
    val coordinates: List<Double>,
    val type: String
)