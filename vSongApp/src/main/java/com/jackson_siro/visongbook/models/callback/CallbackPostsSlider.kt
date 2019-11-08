package com.jackson_siro.visongbook.models.callback

import java.util.ArrayList
import com.jackson_siro.visongbook.models.PostsSlider
import java.io.Serializable

internal class CallbackPostsSlider : Serializable {
    var data: List<PostsSlider> = ArrayList()
}
