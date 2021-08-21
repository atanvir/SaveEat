package com.saveeat.utils.application

import android.R
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.saveeat.ui.activity.auth.signup.SignUpActivity
import com.saveeat.ui.dialog.CreditDialog
import com.saveeat.ui.dialog.ErrorDialog
import com.saveeat.utils.extn.snack
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object ErrorUtil {
    fun handlerGeneralError(view: View?, throwable: Throwable) {
        throwable.printStackTrace()
        if (view == null) return
        when (throwable) {
            is ConnectException ->  snackView(view,"Please turn on Internet")
            is SocketTimeoutException -> snackView(view,"Socket Time Out Exception")
            is UnknownHostException -> snackView(view,"No Internet Connection")
            is InternalError -> snackView(view,"Internal Server Error")
            is HttpException -> { snackView(view,"Something went wrong") }
            else -> { snackView(view,"Something went wrong") }
        }
    }

    fun snackView(view: View, message: String) {
        try{
        val dialog= ErrorDialog()
        val bundle= Bundle()
        bundle.putString("message",message)
        dialog.arguments=bundle
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, com.saveeat.R.style.Dialog_NoTitle)
        if(view.context is AppCompatActivity) dialog.show((view.context as AppCompatActivity).supportFragmentManager, "")
        else if(view.context is FragmentActivity) dialog.show((view.context as FragmentActivity).supportFragmentManager, "")
        else if(view.context is Fragment) dialog.show((view.context as Fragment).childFragmentManager, "")

        }catch (e:Exception){

        }
    }
}


