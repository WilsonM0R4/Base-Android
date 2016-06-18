package com.allem.alleminmotion.visacheckout;

import android.support.v4.print.PrintHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TaxiActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webView;
    private String url = "http://allegra.global/app/demo-allegra-services/#/screens/163392651";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.activity_taxi,this);
    }

    @Override
    public void initViews(View root) {
        webView = (WebView)root.findViewById(R.id.webView3);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new MyBrowser());
    }

    private class MyBrowser extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }
    }

    public void onMenu(View view) {
        animate();
    }
}
