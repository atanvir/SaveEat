package com.saveeat.model.response.saveeat.main.filter

import com.saveeat.model.response.saveeat.bean.CuisineBean
import com.saveeat.model.response.saveeat.bean.CuisinesCategoryBean

data class FilterSubCategoryModel(var status:Int?, var message: String?, var data: MutableList<CuisinesCategoryBean?>?)