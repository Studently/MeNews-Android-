package com.example.ly.menews.domain;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport{

    private int id;
    //用户名
    private String account;
    //密码
    private String password;
    //邮箱
    private String email;
    //头像
    private String headshot;
    //兴趣向量
    private String keyword;

    //是否为游客登陆
    private boolean isVisitor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getHeadshot() {
        return headshot;
    }

    public void setHeadshot(String headshot) {
        this.headshot = headshot;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public boolean isVisitor() {
        return isVisitor;
    }

    public void setVisitor(boolean visitor) {
        isVisitor = visitor;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
