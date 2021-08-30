package com.saveeat.ui.bottomsheet.distance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.base.BaseBottomSheet
import com.saveeat.databinding.BottomSheetDistanceBinding
import com.saveeat.model.request.distance.DistanceModel
import com.saveeat.ui.adapter.distance.DistanceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class DistanceBottomSheet(var listner : (String?) ->Unit,var distance: String?) :  BaseBottomSheet<BottomSheetDistanceBinding>(), (String?) -> Unit {

    override fun getBottomSheetBinding(inflater: LayoutInflater, container: ViewGroup?): BottomSheetDistanceBinding = BottomSheetDistanceBinding.inflate(inflater,container,false)

    override fun init() {
        binding.rvDistance.layoutManager=LinearLayoutManager(activity)
        binding.rvDistance.adapter= DistanceAdapter(requireActivity(),getSelectedDistance(),this)

    }

    private fun getSelectedDistance(): MutableList<DistanceModel?>? {
        val list : MutableList<DistanceModel?>? = ArrayList()
        for (i in 1..10){
            when(0.5f*i){
                1.0f,2.0f,3.0f,4.0f,5.0f ->{
                    if(distance?.equals("Please select distance",ignoreCase = true)==false && distance?.isNotEmpty()==true){
                        if(distance?.toFloat()==0.5f*i.toFloat()) list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()).roundToInt(),check = true,visible = true))
                        else list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()).roundToInt(),check = false,visible = true))
                    }else   list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()).roundToInt(),check = false,visible = true))
                }
                else ->{
                    if(distance?.equals("Please select distance",ignoreCase = true)==false && distance?.isNotEmpty()==true){
                        if(distance?.toFloat()==0.5f*i.toFloat()) list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()),check = true,visible = true))
                        else list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()),check = false,visible = true))
                    }else   list?.add(DistanceModel(name =""+ (0.5f * i.toFloat()),check = false,visible = true))
                }
            }
        }

        return list
    }


    override fun initCtrl() {
    }

    override fun observer() {
    }

    override fun invoke(distance: String?) {
        listner?.invoke(distance)
        dismiss()
    }
}