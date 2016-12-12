package com.allegra.handyuvisa;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 12/12/16.
 */

public class Intellilink extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{
    private WebView webIntellink;
    private String url = "https://m.intellilink.spendmanagement.visa.com";
    private String returnURL;
    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_intellilink,this);
    }

    @Override
    public void initViews(View root) {

        webIntellink = (WebView)root.findViewById(R.id.webIntellilink);
        webIntellink.getSettings().setJavaScriptEnabled(true);
        webIntellink.loadUrl(url);
        webIntellink.setWebViewClient(new Intellilink.MyBrowser());
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_intellink);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_intellink);
        back = (ImageButton) root.findViewById(R.id.back_image);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_intellink);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoBack(v);
            }
        });

        arrowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoForward(v);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUp(v);
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
                webIntellink.loadUrl(returnURL);
            }
            loadArrows();
        }

    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        super.onBackPressed();
    }


    private void loadArrows() {

        if (webIntellink.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webIntellink.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webIntellink.canGoBack()) {
            webIntellink.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webIntellink.canGoForward()) {
            webIntellink.goForward();
        }
    }
}
