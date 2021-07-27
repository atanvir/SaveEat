package com.saveeat.ui.fragment.main.cart

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
import com.saveeat.ui.adapter.cart.CartAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {
    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(inflater,container,false)

    override fun init() {
        binding.rvProducts.layoutManager=LinearLayoutManager(requireActivity())
        binding.rvProducts.adapter=CartAdapter(requireActivity())

        binding.clShadowButton.tvButtonLabel.text="Checkout"

        val wordtoSpan: Spannable = SpannableString("Continue to checkout to save ₹427 on this order ")
        wordtoSpan.setSpan(ForegroundColorSpan(Color.GREEN), 28, 33, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.clBilling.tvSaveLabel.text = wordtoSpan
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<ConstraintLayout>(R.id.clAddress)?.visibility= View.GONE
        activity?.findViewById<TextView>(R.id.tvTitle)?.text="Cart"
        activity?.findViewById<TextView>(R.id.tvTitle)?.compoundDrawablePadding=16
        activity?.findViewById<TextView>(R.id.tvTitle)?.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null)
        activity?.findViewById<TextView>(R.id.tvTitle)?.visibility= View.VISIBLE
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }

}