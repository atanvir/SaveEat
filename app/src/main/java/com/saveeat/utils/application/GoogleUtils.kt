package com.saveeat.utils.application

import android.content.Context
import com.saveeat.model.response.Google.autocomplete.AutoCompleteModel
import android.util.Log
import com.google.gson.GsonBuilder
import com.saveeat.model.request.address.PlacesModel
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper.getPrefrenceStringValue
import com.saveeat.repository.cache.PrefrencesHelper.writePrefrencesValue
import com.saveeat.repository.remote.Google.GoogleConstant.AUTO_COMPLETE
import com.saveeat.repository.remote.Google.GoogleConstant.GOOGLE_BASE_URL
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object GoogleUtils {

    fun autocomplete(context : Context,input: String): MutableList<PlacesModel?>? {
        val resultList: MutableList<PlacesModel?>? = ArrayList()
        val types = "establishment"
        var conn: HttpURLConnection? = null
        val jsonResults = StringBuilder()
        try {
            val urlBuilder=StringBuilder(GOOGLE_BASE_URL + AUTO_COMPLETE)
            urlBuilder.append("input=$input")
            urlBuilder.append("&radius=500")
            urlBuilder.append("&location="+getPrefrenceStringValue(context,PreferenceKeyConstants.latitude)+","+getPrefrenceStringValue(context,PreferenceKeyConstants.longitude))
            urlBuilder.append("&types=$types")
            urlBuilder.append("&language=eng")
            urlBuilder.append("&key=${KeyConstants.GOOGLE_PLACE_API}")
            val url = URL(urlBuilder.toString())
            Log.e("url", urlBuilder.toString())
            conn = url.openConnection() as HttpURLConnection
            val `in` = InputStreamReader(conn.inputStream)
            var read: Int
            val buff = CharArray(1024)
            while (`in`.read(buff).also { read = it } != -1) {
                jsonResults.append(buff, 0, read)
            }
            Log.e("result", jsonResults.toString())
            val autoCompleteModel= GsonBuilder().create().fromJson(jsonResults.toString(), AutoCompleteModel::class.java)
            for (i in autoCompleteModel.predictions.indices) {
                resultList?.add(PlacesModel(placeId = autoCompleteModel.predictions[i].place_id,
                                            placeName = autoCompleteModel.predictions[i].structured_formatting.main_text,
                                            spotName = autoCompleteModel.predictions[i].description))
            }

        }
        catch (e: java.lang.Exception) {
            return resultList
        } finally {
            conn?.disconnect()
        }
        return resultList
    }
}