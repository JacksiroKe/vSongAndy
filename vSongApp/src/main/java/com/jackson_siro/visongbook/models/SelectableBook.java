package com.jackson_siro.visongbook.models;

public class SelectableBook extends CategoryModel {
    private boolean isSelected = false;

    public SelectableBook(CategoryModel slider, boolean isSelected) {
        super(slider.getBookid(), slider.getCategoryid(), slider.getTitle(), slider.getTags(), slider.getQcount(),
                slider.getPosition(), slider.getContent(), slider.getBackpath());
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}