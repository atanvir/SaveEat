package com.saveeat.utils.application

import android.R
import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object ErrorUtil {
    fun handlerGeneralError(context: Context?, throwable: Throwable) {
        throwable.printStackTrace()
        if (context == null) return
        when (throwable) {
            is ConnectException -> showCustomSnackBar(context, "Please turn on Internet")
            is SocketTimeoutException -> showCustomSnackBar(context, "Socket Time Out Exception")
            is UnknownHostException -> showCustomSnackBar(context, "No Internet Connection")
            is InternalError -> showCustomSnackBar(context, "Internal Server Error")
            is HttpException -> { showCustomSnackBar(context, "Something went wrong") }
            else -> { showCustomSnackBar(context, "Something went wrong") }
        }
    }

    private fun showCustomSnackBar(context: Context, message: String) {
        val snackbar= Snackbar.make((context as Activity).findViewById(R.id.content), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}


