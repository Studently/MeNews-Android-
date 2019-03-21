package com.example.ly.menews.utils;

import com.example.ly.menews.domain.User;

public class UserManager {

    private static User mUser;

    public static User getCurrentUser(){
        if(mUser == null){
            mUser = new User();
        }
        return mUser;
    }

    public static void setCurrentUser(User user){
        mUser = user;
    }

    public static void clear(){
        mUser = null;
    }
}
