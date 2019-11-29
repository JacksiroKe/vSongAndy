package com.jackson_siro.visongbook.models.callback

import java.util.ArrayList
import com.jackson_siro.visongbook.models.PostModel
import java.io.Serializable

class CallbackPostsLists: Serializable{
    lateinit var data: MutableList<PostModel>
    var total: Int? = -1
    //var data: List<PostModel>? = ArrayList()
}
