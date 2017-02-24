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
 * Created by jsandoval on 12/12/16.
 */

public class Myvisaenterprise extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {
    private WebView webmyvisa;
    private String url = "https://visaempresarial.com/co/";
    private String returnURL;
    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_myvisa,this);
    }

    @Override
    public void initViews(View root) {

        webmyvisa = (WebView)root.findViewById(R.id.webmyvisa);
        webmyvisa.getSettings().setJavaScriptEnabled(true);
        webmyvisa.getSettings().setAllowContentAccess(false);
        webmyvisa.getSettings().setAllowFileAccess(false);
        webmyvisa.getSettings().setAllowFileAccessFromFileURLs(false);
        webmyvisa.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webmyvisa.loadUrl(url);
        webmyvisa.setWebViewClient(new Myvisaenterprise.MyBrowser());
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_myvisa);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_myvisa);
        back = (ImageButton) root.findViewById(R.id.back_image);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_myvisa);

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
                webmyvisa.loadUrl(returnURL);
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

        if (webmyvisa.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webmyvisa.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webmyvisa.canGoBack()) {
            webmyvisa.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webmyvisa.canGoForward()) {
            webmyvisa.goForward();
        }
    }
}
