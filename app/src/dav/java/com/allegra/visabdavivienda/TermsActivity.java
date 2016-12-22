package com.allegra.handyuvisa;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jsandoval on 2/06/16.
 */
public class TermsActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{

    private WebView webLegal;
    private String url = "http://iataiapps.com/davivienda/terminos-y-condiciones.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_terms_conditions,this);

    }

    @Override
    public void initViews(View root) {
        webLegal = (WebView)root.findViewById(R.id.webview_legal_terms);
        webLegal.getSettings().setJavaScriptEnabled(true);
        webLegal.loadUrl(url);
        webLegal.setWebViewClient(new MyBrowser());
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
