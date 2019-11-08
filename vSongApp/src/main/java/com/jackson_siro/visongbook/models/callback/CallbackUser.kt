package com.jackson_siro.visongbook.models.callback

import com.jackson_siro.visongbook.models.UserModel
import java.io.Serializable

internal class CallbackUser : Serializable {
    lateinit var data: UserModel
    //var data: UserModel? = null
}
