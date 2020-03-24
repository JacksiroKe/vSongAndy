package com.jackson_siro.visongbook.models;

import java.io.Serializable;

public class CategoryModel implements Serializable{
    public int bookid;
    public int categoryid;
    public String title;
    public String tags;
    public String qcount;
    public String position;
    public String content;
    public String backpath;

    public CategoryModel(int bookid, int categoryid, String title, String tags, String qcount, String position, String content, String backpath) {
        this.bookid = bookid;
        this.categoryid = categoryid;
        this.title = title;
        this.tags = tags;
        this.qcount = qcount;
        this.position = position;
        this.content = content;
        this.backpath = backpath;
    }

    public void setBookid(int bookid) {
        this.bookid = bookid;
    }
    public int getBookid() {
        return bookid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }
    public int getCategoryid() {
        return categoryid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }


    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getTags() {
        return tags;
    }


    public void setQcount(String qcount) {
        this.qcount = qcount;
    }
    public String getQcount() {
        return qcount;
    }


    public void setPosition(String position) {
        this.position = position;
    }
    public String getPosition() {
        return position;
    }


    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }


    public void setBackpath(String backpath) {
        this.backpath = backpath;
    }
    public String getBackpath() {
        return backpath;
    }

}
