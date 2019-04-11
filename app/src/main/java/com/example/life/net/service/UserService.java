package com.example.life.net.service;

import android.util.Log;

import com.example.life.entity.UserEntity;
import com.example.life.util.OKHttpUitls;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserService {
    public OKHttpUitls loginHttp;
    public OKHttpUitls verifyHttp;
    public OKHttpUitls registerHttp;
    public OKHttpUitls queryByIdHttp;
    public void login( String content, String password, Integer type){
        loginHttp = new OKHttpUitls();
        JSONObject object = new JSONObject();
        try {
            object.put("content",content);
            object.put("password",password);
            object.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loginHttp.post("http://192.168.8.39:7070/user/login", object.toString());
    }
    public void verification(String content){
        verifyHttp = new OKHttpUitls();
        JSONObject object = new JSONObject();
        try {
            object.put("email",content);
            object.put("type",1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        verifyHttp.post("http://192.168.8.39:7070/user/getVerificationCode", object.toString());
    }
    public void register(UserEntity user){
        registerHttp = new OKHttpUitls();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        registerHttp.post("http://192.168.8.39:7070/user/add", json);
    }


    public void queryUserById(String id){
        queryByIdHttp = new OKHttpUitls();
        JSONObject object = new JSONObject();
        try {
            object.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryByIdHttp.post("http://192.168.8.39:7070/user/queryUserById", object.toString());
    }
}
