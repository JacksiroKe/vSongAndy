package com.jackson_siro.visongbook.models

class SelectableBook(slider: CategoryModel, isSelected: Boolean) : CategoryModel(slider.bookid, slider.categoryid, slider.title, slider.tags, slider.qcount, slider.position, slider.content, slider.backpath) {
    var isSelected = false

    init {
        this.isSelected = isSelected
    }
}