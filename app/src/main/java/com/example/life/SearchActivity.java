package com.example.life;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SearchActivity extends Fragment {

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //不同的Activity对应不同的布局
        View view = inflater.inflate(R.layout.search_fragement, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * TODO 实现底部菜单对应布局控件事件
         * */
        initWebView();

    }
    private void initSetting(WebView webView){
        WebSettings settings=webView.getSettings();

        // webview启用javascript支持 用于访问页面中的javascript
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);


        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        //断网情况下加载本地缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //让WebView支持DOM storage API
        settings.setDomStorageEnabled(true);

        //让WebView支持缩放
        settings.setSupportZoom(true);

        //启用WebView内置缩放功能
        settings.setBuiltInZoomControls(true);

        //让WebView支持可任意比例缩放
        settings.setUseWideViewPort(true);

        //让WebView支持播放插件
        settings.setPluginState(WebSettings.PluginState.ON);

        //设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        //设置在WebView内部是否允许访问文件
        settings.setAllowFileAccess(true);

        //设置WebView的访问UserAgent
//        settings.setUserAgentString(String string);

        //设置脚本是否允许自动打开弹窗
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= 19) {
                settings.setLoadsImagesAutomatically(true);
            } else {
                settings.setLoadsImagesAutomatically(false);
            }

        // 开启Application H5 Caches 功能
        settings.setAppCacheEnabled(true);

        // 设置编码格式
        settings.setDefaultTextEncodingName("utf-8");
    }
    private void initWebView(){
        webView = (WebView)getActivity().findViewById(R.id.WebView);
        initSetting(webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.loadUrl("http://192.168.8.38:9528/");
//        webView.loadUrl("http://baidu.com");
        Toast.makeText(getActivity(), "加载中", Toast.LENGTH_SHORT).show();
    }
}
