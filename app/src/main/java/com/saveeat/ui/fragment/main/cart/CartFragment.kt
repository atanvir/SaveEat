package com.saveeat.ui.fragment.main.cart

import android.content.Intent
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseFragment
import com.saveeat.databinding.FragmentCartBinding
import com.saveeat.ui.activity.order.checkout.CheckoutActivity
import com.saveeat.ui.adapter.cart.CartAdapter
import com.saveeat.ui.dialog.DatePickerFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>(), View.OnClickListener {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false)

    override fun init() {

        binding.rvProducts.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvProducts.adapter=CartAdapter(requireActivity())

        binding.clShadowButton.tvButtonLabel.text=getString(R.string.checkout)
        val wordtoSpan: Spannable = SpannableString("Continue to checkout to save ₹427 on this order")
        wordtoSpan.setSpan(ForegroundColorSpan(Color.rgb(0, 178, 17)), 28, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.clBilling.tvSaveLabel.text = wordtoSpan
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.GONE
        activity?.findViewById<TextView>(R.id.tvTitle)?.text=getString(R.string.cart)
        activity?.findViewById<TextView>(R.id.tvTitle)?.compoundDrawablePadding=16
        activity?.findViewById<TextView>(R.id.tvTitle)?.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.VISIBLE
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
        binding.clBilling.taxInfo.setOnClickListener(this)
        binding.clBilling.clTaxInfo.ivCLose.setOnClickListener(this)
        binding.btnPickLater.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton ->{ startActivity(Intent(requireActivity(),CheckoutActivity::class.java)) }
            R.id.taxInfo ->{ binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.VISIBLE }
            R.id.ivCLose ->{ binding.clBilling.clTaxInfo.clTaxInfo.visibility=View.GONE }
            R.id.btnPickLater ->{ DatePickerFragment().show(requireActivity().supportFragmentManager,"") }
        }
    }

}