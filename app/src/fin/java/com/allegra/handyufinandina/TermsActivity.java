package com.allegra.handyuvisa;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by jsandoval on 2/06/16.
 */
public class TermsActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener{

    private WebView webLegal;
    private String url = "http://allegra.global/terminos-y-condiciones/legales-app-allegra/terminos-y-condiciones.html";

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
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    public void onMenu(View view) {
        animate();
    }
}
