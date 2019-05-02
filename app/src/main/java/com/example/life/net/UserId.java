package com.example.life.net;

import android.app.Application;

public class UserId extends Application {
    private String userId;

    public String getUserId(){
        return this.userId;
    }
    public void setUserId(String userId){
        this.userId= userId;
    }
    @Override
    public void onCreate(){
        userId = "";
        super.onCreate();
    }
}
