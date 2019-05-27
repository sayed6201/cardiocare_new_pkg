package com.daffodil.cardiocare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webview);

        String url = (String) getIntent().getStringExtra("url");


        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient());

    }
}
