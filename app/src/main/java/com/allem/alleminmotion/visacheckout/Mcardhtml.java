package com.allem.alleminmotion.visacheckout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 10/06/16.
 */
public class Mcardhtml extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webmcard;
    private ProgressBar progressBar;
    private ImageButton arrowBack, arrowF;
    private String returnURL;
    private String url = "http://allegra.global/membresias/planes/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_mcard_html,this);

    }

    @Override
    public void initViews(View root) {
        webmcard = (WebView)root.findViewById(R.id.webmcard);
        webmcard.getSettings().setJavaScriptEnabled(true);
        webmcard.loadUrl(url);
        webmcard.setWebViewClient(new MyBrowser(this));
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back_mcard);
        arrowF= (ImageButton)root.findViewById(R.id.arrow_foward_mcard);
        progressBar = (ProgressBar)root.findViewById(R.id.progressBar);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webmcard.goBack();

            }
        });
    }

    private class MyBrowser extends WebViewClient {

        private Context context;
        public MyBrowser(Context context) {
            this.context = context;
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            if(url.equals("allegra:phone")){
                Intent i = new Intent(context, CallActivity.class);
                context.startActivity(i);
                return true;
        }
            return super.shouldOverrideUrlLoading(webView, url);
    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        if (webmcard.canGoBack()) {
            webmcard.goBack();
        }
    }

    private void loadArrows(){

        if(webmcard.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(webmcard.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }

    public void onPageFinished(WebView view, String url) {
        progressBar.setVisibility(View.GONE);
        if (url.equals("about:blank")) {
            webmcard.loadUrl(returnURL);
        }
        loadArrows();
    }
    }
}
