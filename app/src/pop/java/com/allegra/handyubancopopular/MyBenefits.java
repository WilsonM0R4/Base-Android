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
 * Created by jsandoval on 25/07/16.
 */
public class MyBenefits extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webBenefits;
    private String url = "http://allegra.global/visa/app/es/beneficios/index.html";
    private String returnURL;
    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;

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
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_benefits);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_benefits);
        back = (ImageButton) root.findViewById(R.id.back_image);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_benefits);

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
                webBenefits.loadUrl(returnURL);
            }
            loadArrows();
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


    private void loadArrows() {

        if (webBenefits.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webBenefits.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webBenefits.canGoBack()) {
            webBenefits.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webBenefits.canGoForward()) {
            webBenefits.goForward();
        }
    }



}
