package com.example.life.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BasicHttpClient {

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    public static String post(String url, String json) {
        String data = null;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
             response = client.newCall(request).execute();
             data = response.body().string();
             System.out.println("data:" + data);
        } catch (IOException e) {
            System.out.println("e:" + e);
            e.printStackTrace();
        }finally {
            return data;
        }
    }

   public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
       client.newCall(request).enqueue(new Callback() {


           @Override
           public void onFailure(Call call, IOException e) {

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               System.out.println("aaaaaaaaaaaaaaaaa");
               if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

               Headers responseHeaders = response.headers();
               for (int i = 0; i < responseHeaders.size(); i++) {
                   System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
               }

               System.out.println("body:" + response.body().string());
           }
       });
       return null;
    }
}
