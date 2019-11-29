package com.jackson_siro.visongbook.models;

public class SliderModel {
    private String itemID;
    private String itemNumber;
    private String itemTitle;
    private String itemSlug;
    private String itemSongs;

    public SliderModel(String itemID, String itemNumber, String itemTitle, String itemSlug, String itemSongs) {
        this.itemID = itemID;
        this.itemNumber = itemNumber;
        this.itemTitle = itemTitle;
        this.itemSlug = itemSlug;
        this.itemSongs = itemSongs;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }
    public String getItemID() {
        return itemID;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getItemSongs() {
        return itemSongs;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemSlug() {
        return itemSlug;
    }

    public void setItemSlug(String itemSlug) {
        this.itemSlug = itemSlug;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemSongs(String itemSongs) {
        this.itemSongs = itemSongs;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        SliderModel sliderModelCompare = (SliderModel) obj;
        if(sliderModelCompare.getItemTitle().equals(this.getItemTitle()))
            return true;

        return false;
    }
}
