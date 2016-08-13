package com.manhdong.recycler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewNews extends AppCompatActivity {

    WebView myWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        myWeb = (WebView) findViewById(R.id.webView);
        Intent intent = getIntent();
        String toOpen = intent.getStringExtra("Link");
        //myWeb.setClickable(false);
        myWeb.setWebViewClient(new WebViewClient());

        myWeb.loadUrl(toOpen);
//        myWeb.setWebViewClient(new myWebClient(this));




        //myWeb.loadUrl();
    }
}
