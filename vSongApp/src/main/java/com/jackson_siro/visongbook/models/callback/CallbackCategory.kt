package com.jackson_siro.visongbook.models.callback

import java.util.ArrayList
import com.jackson_siro.visongbook.models.CategoryModel
import java.io.Serializable

class CallbackCategory : Serializable {
    lateinit var data: MutableList<CategoryModel>
    //var data: List<CategoryModel> = ArrayList()
}
