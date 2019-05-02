package com.example.life.net.service;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.life.R;
import com.example.life.adapter.WarmClockAdapt;
import com.example.life.entity.ClockEntity;
import com.example.life.result.ModelResult;
import com.example.life.util.OKHttpUitls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import static android.support.constraint.Constraints.TAG;

public class LocationService {


    //    final String hostAndPort = "http://192.168.8.39:7070";
    final String hostAndPort = "http://120.77.86.76:6060";
    public void addLocation(String uid, double longitude, double latitude){
        if(uid.equals("")){
            return;
        }
        OKHttpUitls okHttpUitls = new OKHttpUitls();
        JSONObject object = new JSONObject();
        try {
            object.put("uid",uid);
            object.put("longitude",longitude);
            object.put("latitude",latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        okHttpUitls.post(hostAndPort + "/location/add", object.toString());
        okHttpUitls.setOnOKHttpGetListener(new OKHttpUitls.OKHttpGetListener() {
            @Override
            public void error(String error) {
                Log.d(TAG, "error: add location error");
            }

            @Override
            public void success(String json) {
                Log.d(TAG, "success: add location success");
            }
        });
    }

}
