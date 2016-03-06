package com.liu.happygrow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Liu on 2016/3/6.
 */
public class WebViewActivity extends Activity{
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url=getIntent().getStringExtra("url");
        WebView webView=new WebView(this);
        webView.loadUrl(url);
        setContentView(webView);
    }
}
