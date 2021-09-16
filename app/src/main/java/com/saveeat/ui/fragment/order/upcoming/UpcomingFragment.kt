package com.saveeat.ui.fragment.order.upcoming

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentUpcomingBinding
import com.saveeat.model.request.order.OrderCancelModel
import com.saveeat.model.request.order.OrderHistoryModel
import com.saveeat.model.request.order.OrderStatusModel
import com.saveeat.model.response.saveeat.order.OrderBean
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.order.HistoryOrderAdapter
import com.saveeat.ui.adapter.order.UpcomingOrderAdapter
import com.saveeat.ui.dialog.order.CancelOrderDialog
import com.saveeat.ui.fragment.order.history.HistoryViewModel
import com.saveeat.utils.application.CustomLoader
import com.saveeat.utils.application.ErrorUtil
import com.saveeat.utils.application.KeyConstants
import com.saveeat.utils.application.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UpcomingFragment : BaseFragment<FragmentUpcomingBinding>(), (Int) -> Unit, () -> Unit {
    private val viewModel : UpcomingViewModel by viewModels()
    private var list: MutableList<OrderBean?>? = ArrayList()
    private var position: Int?=0

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentUpcomingBinding = FragmentUpcomingBinding.inflate(inflater,container,false)

    override fun init() {
        CustomLoader.showLoader(requireActivity())
        viewModel.getOrderList(OrderHistoryModel(latitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.latitude).toDouble(),
                                                 longitude= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.longitude).toDouble(),
                                                 orderType = "Running",
                                                 token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                                 userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))

    }

    override fun initCtrl() {
    }

    override fun observer() {
        lifecycleScope.launch {
            viewModel.getOrderList.observe(this@UpcomingFragment,{
                CustomLoader.hideLoader()
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {
                            list=it.value?.data
                            binding.rvUpcomingOrders.layoutManager= LinearLayoutManager(requireActivity())
                            binding.rvUpcomingOrders.adapter= UpcomingOrderAdapter(requireActivity(),list,this@UpcomingFragment)
                        }
                        else if(KeyConstants.FAILURE<=it.value?.status?:0) { ErrorUtil.snackView(binding.root, it.value?.message ?: "") }
                    }
                    is Resource.Failure -> { ErrorUtil.handlerGeneralError(binding.root, it.throwable!!) }
                }

            })
            viewModel.orderCancelByUser.observe(this@UpcomingFragment,{
                when (it) {
                    is Resource.Success -> {
                        if(KeyConstants.SUCCESS==it.value?.status?:0) {

                            val intent= Intent("com.saveeat")
                            requireActivity().sendBroadcast(intent)

                            list?.removeAt(position?:0)
                            binding.rvUpcomingOrders.adapter?.notifyItemRemoved(position?:0)
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

        val dialog= CancelOrderDialog(this)
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, com.saveeat.R.style.Dialog_NoTitle);
        dialog.show(requireActivity().supportFragmentManager,"")
    }

    override fun invoke() {
        viewModel.orderCancelByUser(OrderCancelModel(orderId=list?.get(position?:0)?._id,
                                                    token = PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants.jwtToken),
                                                    userId= PrefrencesHelper.getPrefrenceStringValue(requireActivity(), PreferenceKeyConstants._id)))    }

}