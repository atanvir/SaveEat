package com.saveeat.aws

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.saveeat.aws.AWSConstants.IMAGE_URL
import java.io.File
import kotlin.math.roundToInt

class AWSCallback(var image: File?, var listner : AWSListner?): TransferListener {

    override fun onError(id: Int, e: Exception) {
        listner?.onAWSLoader(false)
        listner?.onAWSError(e.message?:"")
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
        listner?.onAWSProgress(((bytesCurrent.toDouble() / bytesTotal.toDouble()) * 100).roundToInt())
    }

    override fun onStateChanged(id: Int, state: TransferState) {
        if(state != TransferState.IN_PROGRESS) {
            listner?.onAWSLoader(false)
            when (state) {
                TransferState.COMPLETED -> { listner?.onAWSSuccess(IMAGE_URL+image?.name) }
                TransferState.CANCELED, TransferState.FAILED -> { listner?.onAWSError("AWS "+state.name) }
                TransferState.WAITING_FOR_NETWORK -> { listner?.onAWSError("Please turn on internet") }
                else -> { listner?.onAWSError(state.name) }
            }
        }
    }
}