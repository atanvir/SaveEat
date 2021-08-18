package com.saveeat.model.request.auth.login

data class LoginModel(var countryCode: String, var mobileNumber: String,var password: String?,var deviceType: String?,var deviceToken: String?)