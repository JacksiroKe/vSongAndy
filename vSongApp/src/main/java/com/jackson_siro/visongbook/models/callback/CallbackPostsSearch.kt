package com.jackson_siro.visongbook.models.callback

import java.util.ArrayList
import java.io.Serializable
import com.jackson_siro.visongbook.models.PostsSearch

internal class CallbackPostsSearch : Serializable {
    var data: List<PostsSearch> = ArrayList()
}
