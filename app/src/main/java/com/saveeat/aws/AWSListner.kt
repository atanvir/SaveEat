package com.saveeat.aws

interface AWSListner {
    fun onAWSLoader(isLoader: Boolean)
    fun onAWSSuccess(url: String?)
    fun onAWSError(error: String?)
    fun onAWSProgress(progress : Int?)
}