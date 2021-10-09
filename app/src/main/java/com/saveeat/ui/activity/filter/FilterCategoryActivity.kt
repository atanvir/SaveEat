package com.saveeat.ui.activity.filter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.saveeat.R
import com.saveeat.base.BaseActivity
import com.saveeat.databinding.ActivityFilterCategoryBinding
import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.location.LocationModel
import com.saveeat.repository.cache.PrefrencesHelper
import com.saveeat.ui.adapter.filter.FilterCategoryAdapter
import com.saveeat.utils.application.CommonUtils.toolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterCategoryActivity : BaseActivity<ActivityFilterCategoryBinding>(), (Int?) -> Unit, View.OnClickListener {
    private var list: MutableList<CuisineBean?>?=ArrayList()

    override fun getActivityBinding(): ActivityFilterCategoryBinding = ActivityFilterCategoryBinding.inflate(layoutInflater)

    override fun inits() {
        toolbar(this)
        binding.clShadowButton.tvButtonLabel.text="Apply"
        list=intent?.getParcelableArrayListExtra("data")
        list=list?.distinctBy { Pair(it?.name, it?.name) }?.toMutableList()
        binding.rvCategory.layoutManager=LinearLayoutManager(this)
        binding.rvCategory.adapter=FilterCategoryAdapter(this,list,this)
    }

    override fun initCtrl() {
        binding.clShadowButton.ivButton.setOnClickListener(this)
    }

    override fun observer() {
    }

    override fun invoke(position: Int?) {
        list?.get(position?:0)?.check=!(list?.get(position?:0)?.check!!)
        binding.rvCategory.adapter?.notifyItemChanged(position?:0,list?.get(position?:0))
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivButton -> {
                val intent=Intent()
                intent.putParcelableArrayListExtra("data",ArrayList(list))
                setResult(RESULT_OK,intent)
                finish()
            }
        }
    }


}