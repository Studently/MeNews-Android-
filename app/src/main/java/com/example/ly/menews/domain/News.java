package com.example.ly.menews.domain;

import org.litepal.crud.LitePalSupport;

public class News extends LitePalSupport{
    private int id;
    //新闻url
    private String url;
    //新闻标题
    private String title;

    //新闻关键字
    private String keyword;

    //图片
    private String imageUrl;
    //时间
    private String newsTime;
    //新闻类别:新浪、百度……
    private String category;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
