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

public class VisaCheckoutWeb extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{
    private WebView webVisa;
    private String url = "https://secure.checkout.visa.com/createAccount?channel=destination&niflow=destination&nicmp=destination&locale=es-US&country=Co";
    private String returnURL;
    private ImageButton menu, arrowF, arrowB;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.visacheckout_web,this);
    }

    @Override
    public void initViews(View root) {

        webVisa = (WebView)root.findViewById(R.id.webVisaCheckout);
        webVisa.getSettings().setJavaScriptEnabled(true);
        webVisa.getSettings().setAllowContentAccess(false);
        webVisa.getSettings().setAllowFileAccess(false);
        webVisa.getSettings().setAllowFileAccessFromFileURLs(false);
        webVisa.getSettings().setAllowUniversalAccessFromFileURLs(false);
        webVisa.loadUrl(url);
        webVisa.setWebViewClient(new VisaCheckoutWeb.MyBrowser());
        arrowB = (ImageButton) root.findViewById(R.id.arrow_back_visaweb);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_forward_visaweb);
        menu = (ImageButton) root.findViewById(R.id.menu_visa);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_visacheckout);

        arrowB.setOnClickListener(new View.OnClickListener() {
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
                webVisa.loadUrl(returnURL);
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

        if (webVisa.canGoBack()) {
            arrowB.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowB.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webVisa.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webVisa.canGoBack()) {
            webVisa.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webVisa.canGoForward()) {
            webVisa.goForward();
        }
    }
}

