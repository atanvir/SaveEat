package com.saveeat.model.response.saveeat.auth

data class Location(
    val coordinates: List<Double>,
    val type: String
)