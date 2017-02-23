package com.allegra.handyuvisa;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.allegra.handyuvisa.utils.Constants;

/**
 * Created by jsandoval on 2/06/16.
 */
public class TermsActivity extends WebViewActivity implements FrontBackAnimate.InflateReadyListener {

    private String url = Constants.getTermsAndConditionsUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_terms_conditions, this);
    }

    @Override
    public void initViews(View root) {
        setupWebView(root);
    }

    public void onMenu(View view) {
        animate();
    }

    private void setupWebView(View root) {
        mWebView = (WebView) root.findViewById(R.id.webview_legal_terms);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new SecureBrowser(this));
        mWebView.loadUrl(url);
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }
}
