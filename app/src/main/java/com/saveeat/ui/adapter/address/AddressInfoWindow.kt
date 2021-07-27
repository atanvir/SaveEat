package com.saveeat.ui.adapter.address

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.saveeat.databinding.AdapterAddressInfoWindowBinding

class AddressInfoWindow(var context: Context?) : GoogleMap.InfoWindowAdapter {
    override fun getInfoWindow(p0: Marker): View? {
        var binding: ViewDataBinding? = null
        if (p0.title ?: "" is String) {
            binding = AdapterAddressInfoWindowBinding.inflate(LayoutInflater.from(context), null, false)
            binding?.setVariable(BR.data, p0.title)
            binding?.executePendingBindings()
        }
        return if (binding == null) null else binding?.root
    }

    override fun getInfoContents(p0: Marker): View? {
        return null
    }
}