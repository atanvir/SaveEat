package com.saveeat.ui.activity.drawer.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityStaticContentBinding
import com.saveeat.repository.cache.PreferenceKeyConstants
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint
import android.webkit.WebResourceRequest
import android.webkit.WebView

import android.webkit.WebViewClient
import java.lang.Exception


@AndroidEntryPoint
class StaticContentActivity : BaseActivity<ActivityStaticContentBinding>() {
    override fun getActivityBinding(): ActivityStaticContentBinding = ActivityStaticContentBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        when(intent.getStringExtra("type")){
            PreferenceKeyConstants.about_us ->{ binding.webView.loadUrl("http://13.234.94.41/about-us.html") }
            PreferenceKeyConstants.term_condition ->{ binding.webView.loadUrl("http://13.234.94.41/terms.html") }
            PreferenceKeyConstants.privacy_policy ->{ binding.webView.loadUrl("http://13.234.94.41/privacy.html") }
            PreferenceKeyConstants.refund_cancellation ->{ binding.webView.loadUrl("http://13.234.94.41/cancellations.html") }
        }
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                binding.progresBar.visibility= View.GONE
                super.onPageFinished(view, url)
            }
        }
    }

    override fun initCtrl() {
    }

    override fun observer() {
    }
}