package com.jackson_siro.visongbook.models.callback

import java.util.ArrayList
import com.jackson_siro.visongbook.models.Comment
import java.io.Serializable

internal class CallbackShowComment : Serializable {
    var data: List<Comment> = ArrayList()
}
