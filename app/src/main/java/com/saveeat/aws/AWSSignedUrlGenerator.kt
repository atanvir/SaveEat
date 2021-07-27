package com.saveeat.aws

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ResponseHeaderOverrides
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object AWSSignedUrlGenerator {

    fun generateS3SignedUrl(NAME: String?, client: AmazonS3Client?): String? {
        val overrideHeader = ResponseHeaderOverrides()
        overrideHeader.contentType = AWSConstants.IMAGE_FORMAT
        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(AWSConstants.BUCKET_NAME, AWSConstants.FOLDER_PATH + File(AWSConstants.FINALE_IMAGE_URL + NAME).name)
        generatePreSignedUrlRequest.method = HttpMethod.GET
        generatePreSignedUrlRequest.expiration = generateExpireDateAWS()
        generatePreSignedUrlRequest.responseHeaders = overrideHeader
        return client?.generatePresignedUrl(generatePreSignedUrlRequest).toString()
    }

    fun generateExpireDateAWS(): Date? {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        val dateFormat: DateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val tomorrowAsString = dateFormat.format(tomorrow)
        val EXPIRY_DATE = tomorrowAsString
        val expiration = Date()
        var msec: Long = expiration.time
        msec += 1000 * 6000 * 6000.toLong()
        val format: DateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        var date: Date?=null
        try {
            date = format.parse(EXPIRY_DATE)
            expiration.time = date.time
        } catch (e: ParseException) {
            expiration.time = msec
        }

        return date
    }

}