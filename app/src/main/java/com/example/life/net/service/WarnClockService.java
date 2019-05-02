package com.example.life.net.service;

import com.example.life.net.BasicHttpClient;

import java.io.IOException;

public class WarnClockService {
    final String url = "http://192.168.8.39:6060/dayAction/addWarmClock";
//    final String url = "http://baidu.com";
    public void addWarmClock(){

        String aa = null;
        try {
            aa = BasicHttpClient.get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("response:" + aa);
    }
}
