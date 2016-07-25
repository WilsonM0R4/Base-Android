package com.allem.alleminmotion.visacheckout;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jsandoval on 25/07/16.
 */
public class MyBenefits extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webBenefits;
    private String url = "http://allegra.global/app/beneficios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_my_benefits,this);

    }

    @Override
    public void initViews(View root) {

        webBenefits = (WebView)root.findViewById(R.id.webBenefits);
        webBenefits.getSettings().setJavaScriptEnabled(true);
        webBenefits.loadUrl(url);
        webBenefits.setWebViewClient(new MyBrowser());

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
