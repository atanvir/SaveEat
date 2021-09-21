package com.saveeat.ui.bottomsheet.menu.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SortedList
import com.saveeat.base.BaseBottomSheet
import com.saveeat.databinding.BottomSheetRestaurantInfoBinding
import com.saveeat.databinding.BottomsheetSearchBinding
import com.saveeat.model.response.saveeat.bean.RestaurantResponseBean
import com.saveeat.model.response.saveeat.menu.MenuItemProductModel
import com.saveeat.ui.adapter.autocomplete.AutoCompleteAddressAdapter
import com.saveeat.ui.adapter.menu.MenuProductAdapter
import com.saveeat.utils.extn.queryChanged
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

@AndroidEntryPoint
class SearchBottomSheet : BaseBottomSheet<BottomsheetSearchBinding>(){
    var cloneList : MutableList<RestaurantResponseBean?>? = ArrayList()
    var list : MutableList<RestaurantResponseBean?>? = ArrayList()
    override fun getBottomSheetBinding(inflater: LayoutInflater, container: ViewGroup?): BottomsheetSearchBinding = BottomsheetSearchBinding.inflate(inflater,container)

    override fun init() {
        binding.svAddress.isIconified = false
        binding.svAddress.queryHint = "Search Dishes"
        var dataList= arguments?.getParcelableArrayList<MenuItemProductModel?>("data")

        for(i in dataList?.indices!!){
            for(j in dataList[i]?.itemList?.indices!!){
                dataList[i]?.itemList?.get(j)?.type="Selling"
            }

            for(k in dataList[i]?.fullPriceItems?.indices!!){
                dataList[i]?.fullPriceItems?.get(k)?.type="Fix"
            }
            var set=HashSet<RestaurantResponseBean?>()
            set.addAll(dataList[i]?.itemList!!)
            set.addAll(dataList[i]?.fullPriceItems!!)

            cloneList?.addAll(set)
        }

        binding.rvSellingItem.layoutManager=GridLayoutManager(requireActivity(),2)
        binding.rvSellingItem.adapter= MenuProductAdapter(requireActivity(),list,cloneList,"")
    }

    override fun initCtrl() {
        binding.svAddress.queryChanged {
            (binding.rvSellingItem.adapter as MenuProductAdapter).filter.filter(it)
        }
    }

    override fun observer() {
    }


}