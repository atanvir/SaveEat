package com.saveeat.ui.activity.drawer.credit

import android.graphics.Typeface
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.DialogFragment
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityCreditBinding
import com.saveeat.ui.dialog.CreditDialog
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreditActivity : BaseActivity<ActivityCreditBinding>(), View.OnClickListener {

    override fun getActivityBinding(): ActivityCreditBinding = ActivityCreditBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        val wordtoSpan: Spannable = SpannableString("SaveEat Credits are used for purchasing food in the app. If you have a campaign code you can press the button above and type it in to get more credits")
        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        wordtoSpan.setSpan(ResourcesCompat.getFont(this!!, R.font.poppins_regular), 16, wordtoSpan.length-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvLabel.text=wordtoSpan
        binding.clShadowButton.tvButtonLabel.text="Invite Friends"
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
              binding.ivInvite.setImageResource(R.drawable.group_invite)
            binding.clShadowButton.clMainShadow.visibility= View.VISIBLE
        },1500)

    }

    override fun initCtrl() {
        binding.btnAddCredit.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnAddCredit ->{
                val dialog = CreditDialog()
                dialog.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_NoTitle);
                dialog.show(supportFragmentManager, "")
            }
        }
    }
}