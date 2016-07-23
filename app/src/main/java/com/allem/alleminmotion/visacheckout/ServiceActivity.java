package com.allem.alleminmotion.visacheckout;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Sergio Farfan on 6/06/16.
 */
public class ServiceActivity extends  FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webView;
    private String url = "http://allegra.global/app/demo-allegra-services/#/screens";
    //Comment for commit
    //TODO: Get location and send : ?lon=111&lat=222  at finish of the url

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.activity_services,this);
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

    //This is a comment

    public void onMenu(View view) {
        animate();
    }
}
