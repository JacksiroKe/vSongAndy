package com.jackson_siro.visongbook.models

class SliderModel(var itemID: String?, var itemNumber: String?, var itemTitle: String?, var itemSlug: String?, var itemSongs: String?) {


    override fun equals(obj: Any?): Boolean {
        if (obj == null)
            return false

        val sliderModelCompare = obj as SliderModel?
        return if (sliderModelCompare!!.itemTitle == this.itemTitle) true else false

    }
}
