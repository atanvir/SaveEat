package com.saveeat.aws

import android.content.Context
import android.text.TextUtils
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtilityOptions
import com.amazonaws.regions.Region
import com.amazonaws.services.s3.AmazonS3Client
import com.saveeat.aws.AWSConstants.BUCKET_NAME
import com.saveeat.aws.AWSConstants.FOLDER_PATH
import com.saveeat.aws.AWSConstants.IDENTITY_POOL_ID
import com.saveeat.aws.AWSConstants.REGION
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File


class AWSUtils(private val context: Context?, private val filePath: String?, val listner: AWSListner?) {
    private var image: File? = null
    private var transferUtility: TransferUtility? = null
    private var client: AmazonS3Client? = null
    init {
       TransferNetworkLossHandler.getInstance(context)
       val credential = CognitoCachingCredentialsProvider(context?.applicationContext, IDENTITY_POOL_ID, REGION)
       client = AmazonS3Client(credential, Region.getRegion(REGION))
       val tuOptions = TransferUtilityOptions()
       tuOptions.transferThreadPoolSize = 10
       transferUtility = TransferUtility.builder().s3Client(client).context(context?.applicationContext).transferUtilityOptions(tuOptions).build()
       startUploading()
    }

    private fun startUploading() {
        CoroutineScope(Dispatchers.Main).launch {
        coroutineScope {
        with(Dispatchers.IO){
        listner?.onAWSLoader(true)
        if (TextUtils.isEmpty(filePath)) {
            listner?.onAWSLoader(false)
            listner?.onAWSError("No Such Image File Found!")
        }
        else{
        try {
            image=Compressor.compress(context!!,File(filePath?:""))
            val observer = transferUtility?.upload(BUCKET_NAME, FOLDER_PATH+image?.name, image)
            observer?.setTransferListener(AWSCallback(image, client, listner))

        } catch (e: Exception) {
            listner?.onAWSLoader(false)
            listner?.onAWSError(e.message ?: "")
        }
        }
        }
        }
        }
    }





}