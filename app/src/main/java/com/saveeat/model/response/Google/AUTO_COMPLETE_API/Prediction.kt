package com.saveeat.model.response.Google.AUTO_COMPLETE_API

data class Prediction(
    val description: String,
    val place_id: String,
    val reference: String,
    val structured_formatting: StructuredFormatting,
)