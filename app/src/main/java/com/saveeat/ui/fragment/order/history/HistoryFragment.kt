package com.saveeat.ui.fragment.order.history

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.FtsOptions
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentHistoryBinding
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.response.saveeat.order.OrderBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.activity.rating.RatingActivity
import com.saveeat.ui.adapter.order.HistoryOrderAdapter
import com.saveeat.ui.adapter.order.UpcomingOrderAdapter
import com.saveeat.ui.adapter.restaurant.SavedRestaurantAdapter
import com.saveeat.ui.fragment.main.home.HomeViewModel
import com.saveeat.utils.application.*
import com.saveeat.utils.extn.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), (Int) -> Unit {
    private val viewModel : HistoryViewModel by viewModels()
    private var list: MutableList<OrderBean?>?=null
    private var position: Int?=0
    private var orderNotificationBroadcast: BroadcastReceiver?=null

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHistoryBinding = FragmentHistoryBinding.inflate(inflater,container,false)

    override fun init() {
        orderListApi()
    }

    private fun orderListApi(){
        binding.clShimmer.shimmerContainer.startShimmer()
        binding.rvHistoryOrders.visibility=View.GONE
        viewModel.getOrderList(OrderHistoryModel(latitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                                                 longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble(),
                                                 orderType = "Delivered",
                                                 token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                                 userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))
    }


    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(orderNotificationBroadcast, IntentFilter("com.saveeat"))
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().unregisterReceiver(orderNotificationBroadcast)
    }


    override fun initCtrl() {
    }

    override fun observer() {
        lifecycleScope.launch {

            orderNotificationBroadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    orderListApi()
                }
            }

            viewModel.getOrderList.observe(this@HistoryFragment,{
                binding.clShimmer.shimmerContainer.visibility= View.GONE
                binding.clShimmer.shimmerContainer.stopShimmer()
                binding.rvHistoryOrders.visibility=View.VISIBLE

                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list=it.value?.data
                            binding.rvHistoryOrders.layoutManager= LinearLayoutManager(requireActivity())
                            binding.rvHistoryOrders.adapter= HistoryOrderAdapter(requireActivity(),list,this@HistoryFragment)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }

            })
        }

    }

    override fun invoke(p1: Int) {
        position=p1
        laucher.launch(Intent(requireActivity(),RatingActivity::class.java).putExtra("_id",list?.get(position?:0)?._id))
    }




    private val laucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){it->
        if(it.resultCode==RESULT_OK){
            requireActivity().toast(it.data?.getStringExtra("message")?:"")
        }
    }

}