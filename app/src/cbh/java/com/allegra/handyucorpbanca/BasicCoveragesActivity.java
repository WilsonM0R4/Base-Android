package com.allegra.handyucorpbanca;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.FrontBackAnimate;
import com.allegra.handyuvisa.MyBenefits;
import com.allegra.handyuvisa.R;

public class BasicCoveragesActivity extends FrontBackAnimate implements
        FrontBackAnimate.InflateReadyListener {

    private WebView webBasicCoverages;
    private String url = "https://easytravelprotection.com/?onepocket=1";
    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;
    private String returnURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_basic_coverages);
        setView(R.layout.activity_basic_coverages, this);
    }

    @Override
    public void initViews(View root) {
        webBasicCoverages = (WebView)root.findViewById(R.id.webBasicCoverages);
        webBasicCoverages.getSettings().setJavaScriptEnabled(true);
        webBasicCoverages.loadUrl(url);
        webBasicCoverages.setWebViewClient(new MyBrowserCoverages());
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


    private class MyBrowserCoverages extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webBasicCoverages.loadUrl(returnURL);
            }
            loadArrows();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //super.onReceivedSslError(view, handler, error);
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

        if (webBasicCoverages.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webBasicCoverages.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webBasicCoverages.canGoBack()) {
            webBasicCoverages.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webBasicCoverages.canGoForward()) {
            webBasicCoverages.goForward();
        }
    }
}
