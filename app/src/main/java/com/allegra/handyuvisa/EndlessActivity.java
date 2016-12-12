package com.allegra.handyuvisa;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

/**
 * Created by jsandoval on 15/09/16.
 */
public class EndlessActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webEndless;
    private String url = "http://allegra.global/visabusiness/app/endless/index.html";
    private String returnURL;
    private ImageButton arrowBack, arrowF, back;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_endless,this);
    }

    @Override
    public void initViews(View root) {

        webEndless = (WebView)root.findViewById(R.id.webEndless);
        webEndless.getSettings().setJavaScriptEnabled(true);
        webEndless.loadUrl(url);
        webEndless.setWebViewClient(new MyBrowser());
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_endless);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_endless);
        back = (ImageButton) root.findViewById(R.id.back_image);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar_endless);

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
                webEndless.loadUrl(returnURL);
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

        if (webEndless.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webEndless.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webEndless.canGoBack()) {
            webEndless.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webEndless.canGoForward()) {
            webEndless.goForward();
        }
    }
}
