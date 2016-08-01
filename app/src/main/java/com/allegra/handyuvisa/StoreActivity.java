package com.allegra.handyuvisa;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 1/08/16.
 */
public class StoreActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webStore;
    private String url = "http://52.203.29.124/store/";
    private String returnURL;
    private ImageButton arrowBack, arrowF;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_store,this);

    }

    @Override
    public void initViews(View root) {

        webStore = (WebView)root.findViewById(R.id.webStore);
        webStore.getSettings().setJavaScriptEnabled(true);
        webStore.loadUrl(url);
        webStore.setWebViewClient(new MyBrowser());
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_store);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_store);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_store);

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

    }

    private class MyBrowser extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webStore.loadUrl(returnURL);
            }
            loadArrows();
        }

    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        if (webStore.canGoBack()) {
            webStore.goBack();
        }
    }


    private void loadArrows() {

        if (webStore.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webStore.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webStore.canGoBack()) {
            webStore.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webStore.canGoForward()) {
            webStore.goForward();
        }
    }

}
