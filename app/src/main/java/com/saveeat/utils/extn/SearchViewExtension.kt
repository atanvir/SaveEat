package com.saveeat.utils.extn

import android.graphics.Typeface
import android.widget.SearchView
import android.widget.TextView

fun androidx.appcompat.widget.SearchView.queryChanged(listener: (String?) -> Unit) {
    this.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText)
            return false
        }

    })

}


fun SearchView.setTypeFace(typeface: Typeface?) {
    val id = context.resources.getIdentifier("android:id/search_src_text", null, null)
    val searchText = this.findViewById(id) as TextView
    searchText.typeface = typeface
}