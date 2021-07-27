package com.saveeat.aws

import com.amazonaws.regions.Regions

object AWSConstants {
    const val IDENTITY_POOL_ID: String = "us-east-2:695d5e43-324a-443f-91a8-b46dfff8d102"
    val REGION: Regions = Regions.US_EAST_2
    const val BUCKET_NAME: String = "foodapp123"
    const val FOLDER_PATH: String ="profilePic/"
    const val IMAGE_FORMAT: String = "image/jpeg"
    const val S3_URL: String = "https://$BUCKET_NAME.s3.ap-south-1.amazonaws.com/"
    const val FINALE_IMAGE_URL: String=S3_URL+FOLDER_PATH
}