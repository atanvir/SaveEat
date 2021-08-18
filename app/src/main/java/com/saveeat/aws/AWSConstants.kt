package com.saveeat.aws

import com.amazonaws.regions.Regions

object AWSConstants {
    const val IDENTITY_POOL_ID: String = "ap-south-1:db4a6538-60ec-42ca-b458-86fb19e7da17"
    val REGION: Regions = Regions.AP_SOUTH_1
    const val BUCKET_NAME: String = "saveeatprofile"
    const val FOLDER_PATH: String ="android/"
    const val IMAGE_URL: String="https://$BUCKET_NAME.s3.amazonaws.com/$FOLDER_PATH"
}