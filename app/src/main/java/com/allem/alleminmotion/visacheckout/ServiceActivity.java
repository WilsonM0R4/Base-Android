package com.allem.alleminmotion.visacheckout;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

/**
 * Created by Sergio Farfan on 6/06/16.
 */
public class ServiceActivity extends  FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webView;
    private String url = "http://allegra.global/app/servicios/search/";
    private ImageButton arrowBack, arrowF;
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
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back);
        arrowF= (ImageButton)root.findViewById(R.id.arrow_forward);
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

    private void loadArrows(){

        if(webView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(webView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onGoBack(View view){
        if(webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void onGoForward(View view){
        if(webView.canGoForward()) {
            webView.goForward();
        }
    }
}
