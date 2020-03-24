package com.jackson_siro.visongbook.models;

public class ItemModel {
    private String Title;
    private String Description;
    private String Tags;
    private int Image;

    public ItemModel(String Title, String Description, String Tags, int Image) {
        this.Title = Title;
        this.Description = Description;
        this.Tags = Tags;
        this.Image = Image;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int Image) {
        this.Image = Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String Tags) {
        this.Tags = Tags;
    }
}
