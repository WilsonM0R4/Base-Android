package com.allegra.handyuvisa;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 15/11/16.
 */

public class MyTips extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webMyTips;
    private String url = "http://iataiapps.com/gnbsudameris/tips/tips.html";
    private String returnURL;
    private ImageButton back, menu;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_mytips,this);
    }

    @Override
    public void initViews(View root) {

        webMyTips = (WebView)root.findViewById(R.id.webMyTips);
        webMyTips.getSettings().setJavaScriptEnabled(true);
        webMyTips.getSettings().setAllowContentAccess(false);
        webMyTips.getSettings().setAllowFileAccess(false);
        webMyTips.getSettings().setAllowFileAccessFromFileURLs(false);
        webMyTips.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webMyTips.loadUrl(url);
        webMyTips.setWebViewClient(new MyBrowser());
        back = (ImageButton) root.findViewById(R.id.back_image);
        menu = (ImageButton) root.findViewById(R.id.menu_close);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_MyTips);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUp(v);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenu(v);
            }
        });

    }

    private class MyBrowser extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webMyTips.loadUrl(returnURL);
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        super.onBackPressed();
    }
}
