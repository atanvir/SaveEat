package com.saveeat.utils.permissions.camera

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.saveeat.BuildConfig
import com.saveeat.utils.permissions.storage.StorageHelper.createFile
import java.io.File


object CameraHelper {

    fun checkCameraPermissions(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    fun requestCameraPermission(onPermissionlaucher: ActivityResultLauncher<Array<String>>){
        onPermissionlaucher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    fun cameraIntent(context: Context, resultLauncher: ActivityResultLauncher<Intent>) : String?{
        val path = createFile(context=context)
        val imageFileUri = FileProvider.getUriForFile(context.applicationContext, BuildConfig.APPLICATION_ID + ".provider", File(path ?: ""))
        val intent = getPickIntent(context, imageFileUri)
        resultLauncher.launch(intent)
        return path
    }


    private fun getPickIntent(context: Context, cameraOutputUri: Uri?): Intent? {
        val intents= ArrayList<Intent>()
        intents.add(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        setCameraIntents(context,intents, cameraOutputUri)
        if (intents.isEmpty()) return null
        val result = Intent.createChooser(intents.removeAt(0), null)
        if (intents.isNotEmpty()) { result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(arrayOf<Parcelable>())) }
        return result
    }


    private fun setCameraIntents(context:Context,cameraIntents: MutableList<Intent>, output: Uri?) : Boolean{
        var ret=false
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val packageManager: PackageManager =context.packageManager
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
         for (res in listCam) {
            val packageName = res.activityInfo.packageName
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(packageName)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
             ret=cameraIntents.add(intent)
        }
        return ret
    }
}