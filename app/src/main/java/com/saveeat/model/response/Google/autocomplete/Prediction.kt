package com.saveeat.model.response.Google.autocomplete

data class Prediction(
    val description: String,
    val place_id: String,
    val reference: String,
    val structured_formatting: StructuredFormatting,
)